package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.*;
import com.domko.kanbanbackendapp.payload.request.CreateBoardRequest;
import com.domko.kanbanbackendapp.payload.request.UpdateBoardRequest;
import com.domko.kanbanbackendapp.service.implementation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/api/board")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BoardController {

    @Autowired
    private BoardServiceImpl boardService;
    @Autowired
    private TeamServiceImpl teamService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserTeamServiceImpl userTeamService;
    @Autowired
    private TaskServiceImpl taskService;
    @Autowired
    private PermissionService permissionService;


    @GetMapping(value = "/all")
    public List<Board> getAllBoards() {
        return boardService.findAllBoards();
    }

    @PostMapping(value = "/create")
    public ResponseEntity<String> createBoard(@RequestBody CreateBoardRequest createBoardRequest) {
        Optional<Team> team = teamService.findTeam(createBoardRequest.getTeamId());
        if (team.isPresent()) {
            if (permissionService.hasPermissionTo(team.get())) {
                boardService.createBoard(createBoardRequest,team.get());
                return new ResponseEntity<>("Board created", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("You do not participate in this team", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Team does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/get")
    public ResponseEntity<List<Board>> getUserBoards() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userService.findByUsername(authentication.getName());
        if (user.isPresent()) {
            System.out.println("user present");
            List<UserTeam> userTeams = userTeamService.findTeamsOfUser(user.get().getId());
            List<Board> boards = new ArrayList<>();
            userTeams.forEach(e -> boards.addAll(e.getTeam().getBoards()));
            return new ResponseEntity<>(boards, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/get/{boardId}")
    public ResponseEntity<Board> getBoardById(@PathVariable("boardId") long boardId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userService.findByUsername(authentication.getName());
        if (user.isPresent()) {
            Optional<Board> board = boardService.findBoard(boardId);
            if (board.isPresent()) {
                return new ResponseEntity<>(board.get(), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

//    @PostMapping(value = "/update", consumes = "application/json;charset=UTF-8")
//    public ResponseEntity<String> updateBoard(@RequestBody UpdateBoardRequest updateBoardRequest) {
//        Optional<Board> board = boardService.findBoard(updateBoardRequest.getBoardId());
//        if (board.isPresent()) {
//            if (permissionService.hasPermissionTo(board.get())) {
//                updateBoardRequest.getColumns().forEach(column -> {
//                    for (int i = 0; i < column.getTasks().size(); i++) {
//                        Optional<Task> task1 = taskService.findById(column.getTasks().get(i).getId());
//                        if (task1.isPresent()) {
//                            task1.get().setPosition(i);
//                            task1.get().setColumn(column);
//                            taskService.save(task1.get());
//                        }
//                    }
//                });
//                return new ResponseEntity<>("Board updated", HttpStatus.CREATED);
//            } else {
//                return new ResponseEntity<>("You do not participate in this board", HttpStatus.FORBIDDEN);
//            }
//        } else {
//            System.out.println("board does not exists");
//            return new ResponseEntity<>("Board does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}
