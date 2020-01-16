package com.domko.kanbanbackendapp.controller;


import com.domko.kanbanbackendapp.model.BColumn;
import com.domko.kanbanbackendapp.model.Task;
import com.domko.kanbanbackendapp.model.Trend;
import com.domko.kanbanbackendapp.model.TrendId;
import com.domko.kanbanbackendapp.payload.request.CreateTaskRequest;
import com.domko.kanbanbackendapp.payload.request.UpdateTaskRequest;
import com.domko.kanbanbackendapp.payload.response.MessageResponse;
import com.domko.kanbanbackendapp.service.implementation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    private TrendServiceImpl trendService;
    @Autowired
    private SimpMessagingTemplate template;

    @GetMapping(value = "/get/{id}")
    public Optional<Task> findById(@PathVariable Long id) {
        return taskService.findById(id);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<String> createTask(@RequestBody CreateTaskRequest createTaskRequest) {
        Optional<BColumn> column = bColumnService.findById(createTaskRequest.getColumnId());
        if (column.isPresent()) {
            if (permissionService.hasPermissionTo(column.get())) {
                Task task = taskService.createTask(column.get(), createTaskRequest);
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

    @PostMapping(value = "/update")
    public ResponseEntity<String> handleTaskAdded(@RequestBody UpdateTaskRequest updateTaskRequest) {
        Optional<Task> task = taskService.findById(updateTaskRequest.getTaskId());
        Optional<BColumn> destColumn = bColumnService.findById(updateTaskRequest.getColumnId());
        if (task.isPresent() && destColumn.isPresent()) {
            if (permissionService.hasPermissionTo(task.get())) {
                if (taskService.updateTask(task.get(), destColumn.get(), updateTaskRequest)) {
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

    @DeleteMapping(value = "/delete")
    public ResponseEntity<String> handleDeleteTask(@RequestBody Long taskId) {
        Optional<Task> task = taskService.findById(taskId);
        if (task.isPresent()) {
            if (permissionService.hasPermissionTo(task.get())) {
                taskService.delete(task.get());
                template.convertAndSend("/topic/board/" + task.get().getColumn().getBoard().getId(), new MessageResponse("board updated"));
                return new ResponseEntity<>("Task Deleted", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Unauthorized", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Task does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}