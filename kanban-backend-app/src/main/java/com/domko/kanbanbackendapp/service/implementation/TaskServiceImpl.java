package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.BColumn;
import com.domko.kanbanbackendapp.model.Task;
import com.domko.kanbanbackendapp.payload.request.UpdateTaskRequest;
import com.domko.kanbanbackendapp.repository.TaskRepository;
import com.domko.kanbanbackendapp.service.BColumnService;
import com.domko.kanbanbackendapp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private BColumnServiceImpl bColumnService;

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

    @Transactional//maybe not needed (to test)
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

    @Transactional//necessary
    public boolean updateTask(Task task, BColumn column, UpdateTaskRequest updateTaskRequest) {
        switch (updateTaskRequest.getOperation()) {
            case ADD:
                long oldColumnId = task.getColumn().getId();
                task.setColumn(column);
                column.getTasks().add(updateTaskRequest.getNewIndex(), task);
                BColumn updatedColumn = bColumnService.save(column);
                updatePositions(updatedColumn.getTasks());
                Optional<BColumn> oldColumn = bColumnService.findBColumn(oldColumnId);
                if (oldColumn.isPresent()) {
                    oldColumn.get().getTasks().remove(task);
                    BColumn updatedOldColumn = bColumnService.save(oldColumn.get());
                    updatePositions(updatedOldColumn.getTasks());
                    return true;
                } else {
                    return false;
                }
            case MOVE:
                column.getTasks().remove(task);
                column.getTasks().add(updateTaskRequest.getNewIndex(), task);
                updatePositions(column.getTasks());
                return true;
            default:
                return false;
        }
    }


}
