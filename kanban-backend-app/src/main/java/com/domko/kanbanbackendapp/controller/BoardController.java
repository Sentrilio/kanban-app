package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.*;
import com.domko.kanbanbackendapp.payload.request.CreateBoardRequest;
import com.domko.kanbanbackendapp.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/api/board")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> createBoard(@RequestBody CreateBoardRequest createBoardRequest) {
        return boardService.createBoard(createBoardRequest);
    }

    @GetMapping(value = "/get")
    public ResponseEntity<List<Board>> getUserBoards() {
        return boardService.getUserBoards();
    }

    @GetMapping(value = "/get/{boardId}")
    public ResponseEntity<Board> getBoardById(@PathVariable("boardId") long boardId) {
        return boardService.getBoardById(boardId);
    }
}
