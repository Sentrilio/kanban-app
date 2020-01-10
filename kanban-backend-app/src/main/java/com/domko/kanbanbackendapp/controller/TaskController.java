package com.domko.kanbanbackendapp.controller;


import com.domko.kanbanbackendapp.model.BColumn;
import com.domko.kanbanbackendapp.model.Task;
import com.domko.kanbanbackendapp.payload.request.CreateTaskRequest;
import com.domko.kanbanbackendapp.payload.request.UpdateTaskRequest;
import com.domko.kanbanbackendapp.payload.response.MessageResponse;
import com.domko.kanbanbackendapp.service.implementation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

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
    private SimpMessagingTemplate template;

    @GetMapping(value = "/get/{id}")
    public Optional<Task> findById(@PathVariable Long id) {
        return taskService.findById(id);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<String> createTask(@RequestBody CreateTaskRequest createTaskRequest) {
        Optional<BColumn> column = bColumnService.findBColumn(createTaskRequest.getColumnId());
        if (column.isPresent()) {
            if (permissionService.hasPermissionToBColumn(column.get())) {
                Task task = new Task();
                task.setDescription(createTaskRequest.getDescription());
                task.setColumn(column.get());
                task.setPosition(column.get().getTasks().size());
                taskService.save(task);
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
        Optional<BColumn> bColumn = bColumnService.findBColumn(updateTaskRequest.getColumnId());
        if (task.isPresent() && bColumn.isPresent()) {
            if (permissionService.hasPermissionToTask(task.get())) {
                if (taskService.updateTask(task.get(), bColumn.get(), updateTaskRequest)) {
                    template.convertAndSend("/topic/greetings/"+bColumn.get().getBoard().getId(), new MessageResponse("Board updated!"));
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
            if (permissionService.hasPermissionToTask(task.get())) {
                taskService.delete(task.get());
                return new ResponseEntity<>("Task Deleted", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Unauthorized", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Task does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}