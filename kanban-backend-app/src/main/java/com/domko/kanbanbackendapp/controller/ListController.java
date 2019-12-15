package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.model.List;
import com.domko.kanbanbackendapp.payload.request.CreateListRequest;
import com.domko.kanbanbackendapp.service.implementation.BoardServiceImpl;
import com.domko.kanbanbackendapp.service.implementation.ListServiceImpl;
import com.domko.kanbanbackendapp.service.implementation.TeamServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/list")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ListController {

    @Autowired
    private BoardServiceImpl boardService;

    @Autowired
    private ListServiceImpl listService;

    @PostMapping(value = "/create")
    public ResponseEntity<String> createList(@RequestBody CreateListRequest createBoardRequest) {
        Optional<Board> board = boardService.findBoard(createBoardRequest.getBoardId());
        if (board.isPresent()) {
            List list = new List();
            list.setName(createBoardRequest.getListName());
            list.setBoard(board.get());
            listService.save(list);
            return new ResponseEntity<>("List created", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Board does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
