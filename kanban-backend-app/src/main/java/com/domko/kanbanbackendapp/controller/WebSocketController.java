//package com.domko.kanbanbackendapp.controller;
//
//import com.domko.kanbanbackendapp.model.*;
//import com.domko.kanbanbackendapp.payload.request.UpdateTaskRequest;
//import com.domko.kanbanbackendapp.payload.response.MessageResponse;
//import com.domko.kanbanbackendapp.service.implementation.BColumnServiceImpl;
//import com.domko.kanbanbackendapp.service.implementation.PermissionService;
//import com.domko.kanbanbackendapp.service.implementation.TaskServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.messaging.core.MessageSendingOperations;
//import org.springframework.messaging.handler.annotation.DestinationVariable;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.util.HtmlUtils;
//
//import java.time.Period;
//import java.util.Optional;
//
//@Controller
//public class WebSocketController {
//
//    @Autowired
//    private TaskServiceImpl taskService;
//
//    @Autowired
//    private BColumnServiceImpl bColumnService;
//
//    @Autowired
//    private PermissionService permissionService;
//    @Autowired
//    private MessageSendingOperations<String> messagingTemplate;
//
//    @MessageMapping("/hello")
//    @SendTo("/topic/greetings")
//    public Greeting greeting(HelloMessage message) throws Exception {
//        System.out.println("something came up");
//        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
//    }
//
//
//    @MessageMapping("/task/update")
//    public void updateTask(UpdateTaskRequest updateTaskRequest) {
//        Optional<Task> task = taskService.findById(updateTaskRequest.getTaskId());
//        Optional<BColumn> bColumn = bColumnService.findBColumn(updateTaskRequest.getColumnId());
//        if (task.isPresent() && bColumn.isPresent()) {
//            long boardId = bColumn.get().getBoard().getId();
//            if (taskService.updateTask(task.get(), bColumn.get(), updateTaskRequest)) {
//                messagingTemplate.convertAndSend("/topic/greetings/" + boardId, new MessageResponse("board updated!"));
//            } else {
//                System.out.println("Task could not be updated");
//            }
//        } else {
//            System.out.println("task or bcolumn not exists");
//        }
//    }
//    ///
////        System.out.println("something came up in task controller: " + updateTaskRequest);
////        Optional<BColumn> bcolumn = bColumnService.findBColumn(updateTaskRequest.getColumnId());
//
////        if (bcolumn.isPresent()) {
//
////            System.out.println("board id: "+boardId);
////        }
////        return new Greeting("Hello, "+ HtmlUtils.htmlEscape("elo"+ boardId));
////        Optional<Task> task = taskService.findById(updateTaskRequest.getTaskId());
////        Optional<BColumn> bColumn = bColumnService.findBColumn(updateTaskRequest.getColumnId());
////        if (task.isPresent() && bColumn.isPresent()) {
////            if (permissionService.hasPermissionToTask(task.get())) {
////                if (taskService.updateTask(task.get(), bColumn.get(), updateTaskRequest)) {
////                    return task.get().getColumn().getBoard();
////                } else {
////                    return null;
////                }
////            } else {
////                return null;
////            }
////        } else {
////            return null;
////        }
//
//}
