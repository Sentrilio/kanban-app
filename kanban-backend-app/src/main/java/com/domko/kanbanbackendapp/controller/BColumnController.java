package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.model.BColumn;
import com.domko.kanbanbackendapp.payload.request.CreateColumnRequest;
import com.domko.kanbanbackendapp.service.implementation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping(value = "/create")
    public ResponseEntity<String> createColumn(@RequestBody CreateColumnRequest createColumnRequest) {
        Optional<Board> board = boardService.findBoard(createColumnRequest.getBoardId());
        if (board.isPresent()) {
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

}