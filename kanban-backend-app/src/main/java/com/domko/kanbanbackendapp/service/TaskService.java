package com.domko.kanbanbackendapp.service;

import com.domko.kanbanbackendapp.model.BColumn;
import com.domko.kanbanbackendapp.model.Task;
import com.domko.kanbanbackendapp.payload.request.CreateTaskRequest;
import com.domko.kanbanbackendapp.payload.request.UpdateTaskRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    ResponseEntity<String> updateTask(UpdateTaskRequest updateTaskRequest);

    ResponseEntity<String> createTask(CreateTaskRequest createTaskRequest);

    ResponseEntity<String> deleteTask(Long taskId);

    boolean handleTaskUpdate(Task task, BColumn bColumn, UpdateTaskRequest updateTaskRequest);

    void updatePositions(List<Task> tasks);

    Task createTask(BColumn column, CreateTaskRequest createTaskRequest);

    void updateImportance(Task task, BColumn destColumn);

    void incrementImportance(Task task);
}
