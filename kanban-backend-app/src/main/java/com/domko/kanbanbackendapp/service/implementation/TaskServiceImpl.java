package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.BColumn;
import com.domko.kanbanbackendapp.model.Task;
import com.domko.kanbanbackendapp.payload.request.CreateTaskRequest;
import com.domko.kanbanbackendapp.payload.request.UpdateTaskRequest;
import com.domko.kanbanbackendapp.payload.response.MessageResponse;
import com.domko.kanbanbackendapp.repository.TaskRepository;
import com.domko.kanbanbackendapp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@org.springframework.transaction.annotation.Transactional
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private BColumnServiceImpl bColumnService;
    @Autowired
    private TrendServiceImpl trendService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private SimpMessagingTemplate template;


    public ResponseEntity<String> updateTask(UpdateTaskRequest updateTaskRequest) {
        Optional<Task> task = taskRepository.findById(updateTaskRequest.getTaskId());
        Optional<BColumn> destColumn = bColumnService.findById(updateTaskRequest.getColumnId());
        if (task.isPresent() && destColumn.isPresent()) {
            if (permissionService.hasPermissionTo(task.get())) {
                if (handleTaskUpdate(task.get(), destColumn.get(), updateTaskRequest)) {
                    trendService.addTrend(task.get());
                    template.convertAndSend("/topic/board/" + destColumn.get().getBoard().getId(), new MessageResponse("board updated"));
                    return new ResponseEntity<>("Operation " + updateTaskRequest.getOperation() + " on task successful", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Task could not be updated", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>("Unauthorized", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("BColumn does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<String> createTask(CreateTaskRequest createTaskRequest) {
        Optional<BColumn> column = bColumnService.findById(createTaskRequest.getColumnId());
        if (column.isPresent()) {
            if (permissionService.hasPermissionTo(column.get())) {
                Task task = createTask(column.get(), createTaskRequest);
                trendService.addTrend(task);
                template.convertAndSend("/topic/board/" + task.getColumn().getBoard().getId(), new MessageResponse("board updated"));
                return new ResponseEntity<>("Task created", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Board or list does not exists", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Board or list does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> deleteTask(Long taskId) {
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isPresent()) {
            if (permissionService.hasPermissionTo(task.get())) {
                taskRepository.delete(task.get());
                template.convertAndSend("/topic/board/" + task.get().getColumn().getBoard().getId(), new MessageResponse("board updated"));
                return new ResponseEntity<>("Task Deleted", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Unauthorized", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Task does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean handleTaskUpdate(Task task, BColumn destColumn, UpdateTaskRequest updateTaskRequest) {
        switch (updateTaskRequest.getOperation()) {
            case ADD:
                if (destColumn.getTasks().size() < destColumn.getWipLimit()) {
                    updateImportance(task, destColumn);
                    long oldColumnId = task.getColumn().getId();
                    task.setColumn(destColumn);
                    destColumn.getTasks().add(updateTaskRequest.getNewIndex(), task);
                    BColumn updatedColumn = bColumnService.save(destColumn);
                    updatePositions(updatedColumn.getTasks());
                    Optional<BColumn> oldColumn = bColumnService.findById(oldColumnId);
                    if (oldColumn.isPresent()) {
                        oldColumn.get().getTasks().remove(task);
                        BColumn updatedOldColumn = bColumnService.save(oldColumn.get());
                        updatePositions(updatedOldColumn.getTasks());
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    System.out.println("column limit reached");
                    return false;
                }
            case MOVE:
                destColumn.getTasks().remove(task);
                destColumn.getTasks().add(updateTaskRequest.getNewIndex(), task);
                updatePositions(destColumn.getTasks());
                return true;
            default:
                return false;
        }
    }

    public void updatePositions(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            Optional<Task> task = taskRepository.findById(tasks.get(i).getId());
            if (task.isPresent()) {
                task.get().setPosition(i);
                taskRepository.save(task.get());
            } else {
                System.out.println("Task does not exists");
            }
        }
    }

    public Task createTask(BColumn column, CreateTaskRequest createTaskRequest) {
        Task task = new Task();
        task.setDescription(createTaskRequest.getDescription());
        task.setColumn(column);
        task.setImportance(0);
        task.setPosition(column.getTasks().size());
        return taskRepository.save(task);
    }

    public void updateImportance(Task task, BColumn destColumn) {
        if (destColumn.getPosition() < task.getColumn().getPosition()) {
            incrementImportance(task);
        }

    }

    public void incrementImportance(Task task) {
        if (task.getImportance() < 3) {
            task.setImportance(task.getImportance() + 1);
        }
    }

}
