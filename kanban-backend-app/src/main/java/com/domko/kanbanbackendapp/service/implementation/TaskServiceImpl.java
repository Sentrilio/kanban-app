package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.BColumn;
import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.model.Task;
import com.domko.kanbanbackendapp.payload.request.CreateTaskRequest;
import com.domko.kanbanbackendapp.payload.request.UpdateTaskRequest;
import com.domko.kanbanbackendapp.payload.response.MessageResponse;
import com.domko.kanbanbackendapp.repository.BColumnRepository;
import com.domko.kanbanbackendapp.repository.BoardRepository;
import com.domko.kanbanbackendapp.repository.TaskRepository;
import com.domko.kanbanbackendapp.service.PermissionService;
import com.domko.kanbanbackendapp.service.TaskService;
import com.domko.kanbanbackendapp.service.TrendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final BColumnRepository bColumnRepository;
    private final BoardRepository boardRepository;
    private final TrendService trendService;
    private final PermissionService permissionService;
    private final SimpMessagingTemplate template;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, BColumnRepository bColumnRepository, BoardRepository boardRepository, TrendService trendService,
                           PermissionService permissionService, SimpMessagingTemplate template) {
        this.taskRepository = taskRepository;
        this.bColumnRepository = bColumnRepository;
        this.boardRepository = boardRepository;
        this.trendService = trendService;
        this.permissionService = permissionService;
        this.template = template;
    }

    @Override
    public ResponseEntity<String> updateTask(UpdateTaskRequest updateTaskRequest) {
        Optional<Task> task = taskRepository.findById(updateTaskRequest.getTaskId());
        Optional<BColumn> destColumn = bColumnRepository.findById(updateTaskRequest.getColumnId());
        if (task.isPresent() && destColumn.isPresent()) {
            if (permissionService.hasPermissionTo(task.get())) {
                if (handleTaskUpdate(task.get(), destColumn.get(), updateTaskRequest)) {
                    trendService.updateBoardTrends(task.get().getColumn().getBoard());
                    String dest = "/topic/board/" + destColumn.get().getBoard().getId();
                    Optional<Board> board = boardRepository.findById(destColumn.get().getBoard().getId());
                    if (board.isPresent()) {
                        template.convertAndSend(dest, new MessageResponse("board", board.get()));
                        return new ResponseEntity<>("Operation " + updateTaskRequest.getOperation() +
                                " on task successful", HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("Board does not exists", HttpStatus.BAD_REQUEST);
                    }
                } else {
                    System.out.println("task could not be updated.");
//                    String dest = "/topic/board/" + destColumn.get().getBoard().getId();
//                    System.out.println(dest);
//                    template.convertAndSend(dest, new MessageResponse("board updated"));
                    return new ResponseEntity<>("Task could not be updated", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>("Unauthorized", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Task or BColumn does not exists", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> createTask(CreateTaskRequest createTaskRequest) {
        Optional<BColumn> column = bColumnRepository.findById(createTaskRequest.getColumnId());
        if (column.isPresent()) {
            if (permissionService.hasPermissionTo(column.get())) {
                Task task = createTask(column.get(), createTaskRequest);
                if (task != null) {
                    column.get().getTasks().add(task);
                    bColumnRepository.save(column.get());
                    trendService.updateBoardTrends(task.getColumn().getBoard());
                    trendService.updateNumberOfTasks(task.getColumn().getBoard().getId());
                    trendService.incrementArrivalOfTasks(task.getColumn().getBoard().getId());
                    Optional<Board> board = boardRepository.findById(task.getColumn().getBoard().getId());
                    if (board.isPresent()) {
                        String dest = "/topic/board/" + task.getColumn().getBoard().getId();
                        template.convertAndSend(dest, new MessageResponse("board", board.get()));
                        return new ResponseEntity<>("Task created", HttpStatus.CREATED);
                    } else {
                        return new ResponseEntity<>("Board does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                } else {
                    return new ResponseEntity<>("Task could not be created", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>("Board or list does not exists", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Board or list does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public ResponseEntity<String> deleteTask(Long taskId) {
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isPresent()) {
            if (permissionService.hasPermissionTo(task.get())) {
                long boardId = task.get().getColumn().getBoard().getId();
                task.get().getColumn().getTasks().remove(task.get());
                bColumnRepository.save(task.get().getColumn());
                taskRepository.delete(task.get());
                trendService.updateBoardTrends(task.get().getColumn().getBoard());
                trendService.updateNumberOfTasks(boardId);
                Optional<Board> board = boardRepository.findById(boardId);
                if (board.isPresent()) {
                    String dest = "/topic/board/" + boardId;
                    template.convertAndSend(dest, new MessageResponse("board", board.get()));
                    return new ResponseEntity<>("Task Deleted", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Board does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>("Unauthorized", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Task does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean handleTaskUpdate(Task task, BColumn destColumn, UpdateTaskRequest updateTaskRequest) {
        switch (updateTaskRequest.getOperation()) {
            case ADD:
                if (destColumn.getTasks().size() < destColumn.getWipLimit()) {
                    if (updateTaskRequest.getNewIndex() >= 0 && updateTaskRequest.getNewIndex() <= destColumn.getTasks().size()) {
                        updateImportance(task, destColumn);
                        long oldColumnId = task.getColumn().getId();
                        task.setColumn(destColumn);
                        taskRepository.save(task);//?
                        destColumn.getTasks().add(updateTaskRequest.getNewIndex(), task);
                        BColumn updatedColumn = bColumnRepository.save(destColumn);
                        updatePositions(updatedColumn.getTasks());
                        Optional<BColumn> oldColumn = bColumnRepository.findById(oldColumnId);
                        if (oldColumn.isPresent()) {
                            oldColumn.get().getTasks().remove(task);
                            BColumn updatedOldColumn = bColumnRepository.save(oldColumn.get());
                            updatePositions(updatedOldColumn.getTasks());
                            return true;
                        } else {
                            System.out.println("old column not present");
                            return false;
                        }
                    } else {
                        System.out.println("new index: " + updateTaskRequest.getNewIndex());
                        return false;
                    }
                } else {
                    System.out.println("column limit reached");
                    return false;
                }
            case MOVE:
                if (updateTaskRequest.getNewIndex() >= 0 && updateTaskRequest.getNewIndex() <= destColumn.getTasks().size()) {
                    destColumn.getTasks().remove(task);
                    destColumn.getTasks().add(updateTaskRequest.getNewIndex(), task);
                    updatePositions(destColumn.getTasks());
                    return true;
                } else {
                    System.out.println("task could not be moved. New index: " + updateTaskRequest.getNewIndex());
                    return false;
                }
            default:
                return false;
        }
    }

    @Override
    public void updatePositions(List<Task> tasks) {
        List<Task> listWithoutNulls = tasks.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        for (int i = 0; i < listWithoutNulls.size(); i++) {
            Optional<Task> task = taskRepository.findById(listWithoutNulls.get(i).getId());
            if (task.isPresent()) {
                task.get().setPosition(i);
                taskRepository.save(task.get());
            } else {
                System.out.println("Task does not exists");
            }
        }
    }

    @Override
    public Task createTask(BColumn column, CreateTaskRequest createTaskRequest) {
        if (column.getTasks().size() < column.getWipLimit()) {
            Task task = new Task();
            task.setDescription(createTaskRequest.getDescription());
            task.setColumn(column);
            task.setImportance(0);
            task.setPosition(column.getTasks().size());
            return taskRepository.save(task);
        } else {
            return null;
        }
    }

    @Override
    public void updateImportance(Task task, BColumn destColumn) {
        if (destColumn.getPosition() < task.getColumn().getPosition()) {
            incrementImportance(task);
        }

    }

    @Override
    public void incrementImportance(Task task) {
        if (task.getImportance() < 3) {
            task.setImportance(task.getImportance() + 1);
        }
    }

}
