package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.BColumn;
import com.domko.kanbanbackendapp.model.Task;
import com.domko.kanbanbackendapp.payload.request.CreateTaskRequest;
import com.domko.kanbanbackendapp.payload.request.UpdateTaskRequest;
import com.domko.kanbanbackendapp.repository.TaskRepository;
import com.domko.kanbanbackendapp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    @Override
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public void delete(Task task) {
        taskRepository.delete(task);
    }

//    @Transactional//maybe not needed (to test)
    public void updatePositions(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            Optional<Task> task = findById(tasks.get(i).getId());
            if (task.isPresent()) {
                task.get().setPosition(i);
                save(task.get());
            } else {
                System.out.println("Task does not exists");
            }
        }
    }

//    @Transactional//necessary
    public boolean updateTask(Task task, BColumn destColumn, UpdateTaskRequest updateTaskRequest) {
        switch (updateTaskRequest.getOperation()) {
            case ADD:
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
            case MOVE:
                destColumn.getTasks().remove(task);
                destColumn.getTasks().add(updateTaskRequest.getNewIndex(), task);
                updatePositions(destColumn.getTasks());
                return true;
            default:
                return false;
        }
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

    public Task createTask(BColumn column, CreateTaskRequest createTaskRequest) {
        Task task = new Task();
        task.setDescription(createTaskRequest.getDescription());
        task.setColumn(column);
        task.setImportance(0);
        task.setPosition(column.getTasks().size());
        return save(task);
    }
}
