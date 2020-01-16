package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.model.BColumn;
import com.domko.kanbanbackendapp.payload.request.CreateColumnRequest;
import com.domko.kanbanbackendapp.payload.request.UpdateColumnRequest;
import com.domko.kanbanbackendapp.payload.response.MessageResponse;
import com.domko.kanbanbackendapp.service.implementation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

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
    private PermissionService permissionService;
    @Autowired
    private SimpMessagingTemplate template;

    @PostMapping(value = "/create")
    public ResponseEntity<String> createColumn(@RequestBody CreateColumnRequest createColumnRequest) {
        Optional<Board> board = boardService.findBoard(createColumnRequest.getBoardId());
        if (board.isPresent()) {
            if (permissionService.hasPermissionTo(board.get())) {
                BColumn column = bColumnService.createColumn(board.get(), createColumnRequest);
                template.convertAndSend("/topic/greetings/" + column.getBoard().getId(), new MessageResponse("board updated"));
                return new ResponseEntity<>("Column created", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>("Board does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/change-position")
    public ResponseEntity<String> handleBColumnMove(@RequestBody UpdateColumnRequest updateColumnRequest) {
        Optional<BColumn> bColumn = bColumnService.findById(updateColumnRequest.getColumnId());
        if (bColumn.isPresent()) {
            if (permissionService.hasPermissionTo(bColumn.get())) {
                if (bColumnService.updateBColumn(bColumn.get(), updateColumnRequest)) {
                    template.convertAndSend("/topic/board/" + bColumn.get().getBoard().getId(), new MessageResponse("board updated"));
                    return new ResponseEntity<>("Operation " + updateColumnRequest.getOperation() + " on column successful", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Column could not be updated", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>("Unauthorized", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("BColumn does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<String> handleDeleteBColumn(@RequestBody Long column) {
        Optional<BColumn> bColumn = bColumnService.findById(column);
        if (bColumn.isPresent()) {
            if (permissionService.hasPermissionTo(bColumn.get())) {
                bColumnService.delete(bColumn.get());
                template.convertAndSend("/topic/board/" + bColumn.get().getBoard().getId(), new MessageResponse("board updated"));
                return new ResponseEntity<>("Column Deleted", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Unauthorized", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Column does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
