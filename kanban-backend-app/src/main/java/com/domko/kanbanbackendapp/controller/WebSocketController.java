package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.*;
import com.domko.kanbanbackendapp.payload.request.UpdateTaskRequest;
import com.domko.kanbanbackendapp.service.implementation.BColumnServiceImpl;
import com.domko.kanbanbackendapp.service.implementation.PermissionService;
import com.domko.kanbanbackendapp.service.implementation.TaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.HtmlUtils;

import java.time.Period;
import java.util.Optional;

@Controller
public class WebSocketController {

    @Autowired
    private TaskServiceImpl taskService;

    @Autowired
    private BColumnServiceImpl bColumnService;

    @Autowired
    private PermissionService permissionService;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

    @MessageMapping("/task/update/{taskId}")
    @SendTo("/topic/board")
    public Board updateTask(@RequestBody UpdateTaskRequest updateTaskRequest) {
        Optional<Task> task = taskService.findById(updateTaskRequest.getTaskId());
        Optional<BColumn> bColumn = bColumnService.findBColumn(updateTaskRequest.getColumnId());
        if (task.isPresent() && bColumn.isPresent()) {
            if (permissionService.hasPermissionToTask(task.get())) {
                if (taskService.updateTask(task.get(), bColumn.get(), updateTaskRequest)) {
                    return task.get().getColumn().getBoard();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

}
