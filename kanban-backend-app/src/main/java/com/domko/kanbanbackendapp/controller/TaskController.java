package com.domko.kanbanbackendapp.controller;


import com.domko.kanbanbackendapp.model.BColumn;
import com.domko.kanbanbackendapp.model.Task;
import com.domko.kanbanbackendapp.payload.request.CreateTaskRequest;
import com.domko.kanbanbackendapp.service.implementation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/api/task")
public class TaskController {

    @Autowired
    private TaskServiceImpl taskService;

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private ColumnServiceImpl boardListService;

    @GetMapping(value = "/get/{id}")
    public Optional<Task> findById(@PathVariable Long id) {
        return taskService.findById(id);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<String> createTask(@RequestBody CreateTaskRequest createTaskRequest) {
        Optional<BColumn> column = boardListService.findList(createTaskRequest.getColumnId());
        if (column.isPresent()) {
            if (permissionService.hasPermissionToBoardList(column.get())) {
                Task task = new Task();
                task.setDescription(createTaskRequest.getDescription());
                task.setColumn(column.get());
                task.setPosition(column.get().getTasks().size() + 1);
                System.out.println("task info: " + task.getColumn().getName() + "description: " + task.getDescription());
                taskService.saveTask(task);
                return new ResponseEntity<>("Task created", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Board or list does not exists", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Board or list does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}