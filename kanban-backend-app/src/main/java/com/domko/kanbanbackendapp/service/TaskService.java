package com.domko.kanbanbackendapp.service;

import com.domko.kanbanbackendapp.model.BColumn;
import com.domko.kanbanbackendapp.model.Task;
import com.domko.kanbanbackendapp.payload.request.UpdateTaskRequest;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    Optional<Task> findById(Long id);

    Task save(Task task);

    void delete(Task task);

    boolean updateTask(Task task, BColumn bColumn, UpdateTaskRequest updateTaskRequest);
}
