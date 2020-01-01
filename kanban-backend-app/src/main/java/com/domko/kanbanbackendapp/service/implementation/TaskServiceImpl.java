package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.BColumn;
import com.domko.kanbanbackendapp.model.Task;
import com.domko.kanbanbackendapp.payload.request.UpdateTaskRequest;
import com.domko.kanbanbackendapp.repository.TaskRepository;
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
    private TaskServiceImpl taskService;
    @Autowired
    private BColumnServiceImpl bColumnService;

    @Override
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }


    //start-inclusive, end-exclusive
    public void incrementTasksPositions(Integer startIndex, Integer endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            Optional<Task> task = taskRepository.findByPosition(i);
            task.ifPresent(value -> value.setPosition(value.getPosition() + 1));
        }
    }

    //start-inclusive, end-exclusive
    public void decrementTasksPositions(Integer startIndex, Integer endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            Optional<Task> task = taskRepository.findByPosition(i);
            task.ifPresent(value -> value.setPosition(value.getPosition() - 1));
        }
    }

    public boolean updateTask(Task task, BColumn bColumn, UpdateTaskRequest updateTaskRequest) {
        switch (updateTaskRequest.getOperation()) {
            case ADD:
                incrementTasksPositions(updateTaskRequest.getNewIndex(), bColumn.getTasks().size());
                task.setColumn(bColumn);
                task.setPosition(updateTaskRequest.getNewIndex());
                taskService.saveTask(task);
                return true;
            case MOVE:
                if (updateTaskRequest.getOldIndex() > updateTaskRequest.getNewIndex()) {
                    incrementTasksPositions(updateTaskRequest.getNewIndex(), updateTaskRequest.getOldIndex());
                    return true;
                } else if (updateTaskRequest.getNewIndex() > updateTaskRequest.getOldIndex()) {
                    decrementTasksPositions(updateTaskRequest.getOldIndex(), updateTaskRequest.getNewIndex());
                    return true;
                } else {
                    return false;
                }
            case REMOVE:
//                decrementTasksPositions(updateTaskRequest);
                return true;
            default:
                return false;
        }
    }
}
