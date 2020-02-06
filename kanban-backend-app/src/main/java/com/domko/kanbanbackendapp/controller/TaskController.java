package com.domko.kanbanbackendapp.controller;


import com.domko.kanbanbackendapp.payload.request.CreateTaskRequest;
import com.domko.kanbanbackendapp.payload.request.UpdateTaskRequest;
import com.domko.kanbanbackendapp.service.TaskService;
import com.domko.kanbanbackendapp.service.implementation.*;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/api/task")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping(value = "/create")
    public ResponseEntity<String> createTask(@RequestBody CreateTaskRequest createTaskRequest) {
        return taskService.createTask(createTaskRequest);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<String> handleTaskUpdate(@RequestBody UpdateTaskRequest updateTaskRequest) {
        return taskService.updateTask(updateTaskRequest);
    }

    @DeleteMapping(value = "/delete/{taskId}")
    public ResponseEntity<String> handleDeleteTask(@PathVariable("taskId") long taskId) {
        return taskService.deleteTask(taskId);
    }
}