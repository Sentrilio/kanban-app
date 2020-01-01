package com.domko.kanbanbackendapp.controller;


import com.domko.kanbanbackendapp.model.BColumn;
import com.domko.kanbanbackendapp.model.Task;
import com.domko.kanbanbackendapp.payload.request.AddedTaskToBColumnRequest;
import com.domko.kanbanbackendapp.payload.request.CreateTaskRequest;
import com.domko.kanbanbackendapp.payload.request.MoveTaskRequest;
import com.domko.kanbanbackendapp.service.implementation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/api/task")
public class TaskController {

    @Autowired
    private TaskServiceImpl taskService;
    @Autowired
    private BColumnServiceImpl bColumnService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private BColumnServiceImpl boardListService;

    @GetMapping(value = "/get/{id}")
    public Optional<Task> findById(@PathVariable Long id) {
        return taskService.findById(id);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<String> createTask(@RequestBody CreateTaskRequest createTaskRequest) {
        Optional<BColumn> column = boardListService.findBColumn(createTaskRequest.getColumnId());
        if (column.isPresent()) {
            if (permissionService.hasPermissionToBColumn(column.get())) {
                Task task = new Task();
                task.setDescription(createTaskRequest.getDescription());
                task.setColumn(column.get());
                task.setPosition(column.get().getTasks().size());
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

    @PostMapping(value = "/add-to-column")
    public ResponseEntity<String> handleTaskAdded(@RequestBody AddedTaskToBColumnRequest addedTaskToBColumnRequest) {
        Optional<Task> task = taskService.findById(addedTaskToBColumnRequest.getTaskId());
        Optional<BColumn> bColumn = bColumnService.findBColumn(addedTaskToBColumnRequest.getBColumnId());
        if (task.isPresent() && bColumn.isPresent()) {
            if (permissionService.hasPermissionToTask(task.get())) {
                task.get().setColumn(bColumn.get());
                taskService.saveTask(task.get());
                bColumn.get().getTasks().add(addedTaskToBColumnRequest.getNewPosition(), task.get());
                bColumnService.save(bColumn.get());
                return new ResponseEntity<>("Task added to bColumn", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>("BColumn does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/move")
    public ResponseEntity<String> handleTaskMoved(@RequestBody MoveTaskRequest moveTaskRequest) {
        Optional<Task> task = taskService.findById(moveTaskRequest.getTaskId());
        Optional<BColumn> bColumn = bColumnService.findBColumn(moveTaskRequest.getBColumnId());
        if (task.isPresent() && bColumn.isPresent()) {
            if (permissionService.hasPermissionToTask(task.get())) {
                List<Task> tasks = bColumn.get().getTasks();
                tasks.remove(task.get());
                tasks.add(moveTaskRequest.getNewIndex(), task.get());
                bColumn.get().setTasks(tasks);
                bColumnService.save(bColumn.get());
                return new ResponseEntity<>("Task added to bColumn", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>("BColumn does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}