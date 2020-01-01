package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.BColumn;
import com.domko.kanbanbackendapp.model.Task;
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

    @Override
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public void updateAllTasksPositions(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            Optional<Task> task = taskRepository.findById(tasks.get(i).getId());
            if (task.isPresent()) {
                task.get().setPosition(i);
                taskRepository.save(task.get());
            }
        }
    }

    public void updateAllTasksPositionsInBColumn(BColumn bColumn) {
        List<Task> tasks = bColumn.getTasks();
        for (int i = 0; i < tasks.size(); i++) {
            Optional<Task> task = taskRepository.findById(tasks.get(i).getId());
            if (task.isPresent()) {
                task.get().setPosition(i);
                taskRepository.save(task.get());
            }
        }
    }

    public void incrementTasksPositions(BColumn bColumn, Integer startIndex) {
        bColumn.getTasks()
                .forEach(task -> {
                    if (task.getPosition() >= startIndex) {
                        task.setPosition(task.getPosition() + 1);
                        taskRepository.save(task);
                    }
                });
    }

    public void decrementTasksPositions(BColumn bColumn, Integer startIndex) {
        for (int i = 0; i < bColumn.getTasks().size(); i++) {
            Optional<Task> task = taskRepository.findById(bColumn.getTasks().get(i).getId());
            if (task.isPresent()) {
                task.get().setPosition(i);
            }
        }
        bColumn.getTasks()
                .forEach(task -> {
                    if (task.getPosition() > startIndex) {
                        task.setPosition(task.getPosition() - 1);
                        taskRepository.save(task);
                    }
                });
    }
}
