package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.model.BColumn;
import com.domko.kanbanbackendapp.model.Task;
import com.domko.kanbanbackendapp.payload.request.AddedTaskToBColumnRequest;
import com.domko.kanbanbackendapp.payload.request.CreateColumnRequest;
import com.domko.kanbanbackendapp.payload.request.MoveTaskRequest;
import com.domko.kanbanbackendapp.payload.request.RemoveTaskRequest;
import com.domko.kanbanbackendapp.service.implementation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/column")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BColumnController {

    @Autowired
    private BoardServiceImpl boardService;
    @Autowired
    private BColumnServiceImpl bColumnService;
    @Autowired
    private UserTeamServiceImpl userTeamService;

    @Autowired
    private TaskServiceImpl taskService;
    @Autowired
    private PermissionService permissionService;

    @PostMapping(value = "/create")
    public ResponseEntity<String> createColumn(@RequestBody CreateColumnRequest createColumnRequest) {
        Optional<Board> board = boardService.findBoard(createColumnRequest.getBoardId());
        if (board.isPresent()) {
//            List<UserTeam> userTeams = userTeamService.findUsersOfTeam(board.get().getTeam().getId());
//            if (userTeamService.hasPermission(userTeams)) {
            if (permissionService.hasPermissionToBoard(board.get())) {
                BColumn column = new BColumn();
                column.setName(createColumnRequest.getColumnName());
                column.setBoard(board.get());
                column.setPosition(board.get().getColumns().size());
                System.out.println("column name: " + column.getName());
                bColumnService.save(column);
                return new ResponseEntity<>("Column created", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>("Board does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//    @PostMapping(value = "/remove-task")
//    public ResponseEntity<String> updateTasksOrder(@RequestBody RemoveTaskRequest removeTaskRequest) {
//        Optional<BColumn> bColumn = bColumnService.findBColumn(removeTaskRequest.getBColumnId());
//        if (bColumn.isPresent()) {
//            if (permissionService.hasPermissionToBColumn(bColumn.get())) {
//                taskService.decrementTasksPositions(bColumn.get(), removeTaskRequest.getTaskIndex());
////                taskService.updateAllTasksPositionsInBColumn(bColumn.get());
////                taskService.updateAllTasksPositions(updateTasksPositionsRequest.getTasks());
//                return new ResponseEntity<>("Column positions updated", HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
//            }
//        } else {
//            return new ResponseEntity<>("BColumn does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }



}
