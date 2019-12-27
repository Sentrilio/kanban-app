package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.*;
import com.domko.kanbanbackendapp.payload.request.CreateBoardRequest;
import com.domko.kanbanbackendapp.payload.request.UpdateBoardRequest;
import com.domko.kanbanbackendapp.security.jwt.JwtUtils;
import com.domko.kanbanbackendapp.service.implementation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private JwtUtils jwtUtils;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserTeamServiceImpl userTeamService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private TaskServiceImpl taskService;

    @GetMapping(value = "/all")
    public List<Board> getAllBoards() {
        return boardService.findAllBoards();
    }

    @PostMapping(value = "/create")
    public ResponseEntity<String> createBoard(@RequestBody CreateBoardRequest createBoardRequest) {
        Optional<Team> team = teamService.findTeam(createBoardRequest.getTeamId());
        if (team.isPresent()) {
            if (permissionService.hasPermissionToTeam(team.get())) {

                Board board = new Board();
                board.setName(createBoardRequest.getBoardName());
                board.setTeam(team.get());
                if (createBoardRequest.getWipLimit() == null) {
                    board.setWipLimit(5);
                } else {
                    board.setWipLimit(createBoardRequest.getWipLimit());
                }
                boardService.saveBoard(board);
                return new ResponseEntity<>("Board created", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("You do not participate in this team", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Team does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/update", consumes = "application/json;charset=UTF-8")
    public ResponseEntity<String> updateBoard(@RequestBody UpdateBoardRequest updateBoardRequest) {
//        System.out.println(updateBoardRequest.getBoardId());
//        System.out.println(updateBoardRequest.getColumns().toString());
        Optional<Board> board = boardService.findBoard(updateBoardRequest.getBoardId());
        if (board.isPresent()) {
            if (permissionService.hasPermissionToBoard(board.get())) {
                for (BColumn column : updateBoardRequest.getColumns()) {
                    for (int i = 0; i < column.getTasks().size(); i++) {
                        Optional<Task> task1 = taskService.findById(column.getTasks().get(i).getId());
                        if (task1.isPresent()) {
                            task1.get().setPosition(i);
                            task1.get().setColumn(column);
                            taskService.saveTask(task1.get());
                        }
                    }
                }
//                updateBoardRequest.getColumns().forEach(e -> {
//                    e.setBoard(board.get());
//                    System.out.println(e.getName());
//                    System.out.println(e.getBoard().getId());
//                    System.out.println(e.getBoard().getTeam());
//                    System.out.println(e.getId());
//                    System.out.println(e.getPosition());

//                    e.getTasks().forEach(task -> {
//                        Optional<Task> task1 = taskService.findById(task.getId());
//                        if (task1.isPresent()) {
//                            task1.get().setPosition();
//                        }
//                        System.out.println("column of task: " + task.getColumn());
//                        taskService.saveTask(task1.get());
//                        System.out.println(task.getColumn());
//                        System.out.println(task.getDescription() + "position: " + task.getPosition());
//                });
//                });

//                for (BColumn column : updateBoardRequest.getColumns()) {
//                    for (int i = 0; i < column.getTasks().size(); i++) {
//                        column.getTasks().get(i).setPosition(i);
//                    }
//                }

//                board.get().setColumns(updateBoardRequest.getColumns());
//                System.out.println("after");
//                board.get().getColumns().forEach(e -> {
//                    System.out.println(e.getName());
//                    e.getTasks().forEach(task -> {
//                        System.out.println(task.getDescription() + "position: " + task.getPosition());
//                    });
//                });
//                System.out.println(board.get().getColumns().toString());

//                boardToSave.setName(board.get().getName());
//                boardToSave.setTeam(board.get().getTeam());
//                boardToSave.setWipLimit(board.get().getWipLimit());
//                boardToSave.setId(board.get().getId());
//                boardToSave.setColumns(updateBoardRequest.getColumns());
//                board.get().setColumns(updateBoardRequest.getColumns());
//                Board board1 = boardService.saveBoard(board.get());
//                board1.getColumns().forEach(bColumn -> {
//                    bColumn.getTasks().forEach(task -> {
//                        System.out.println(task.getDescription()+": "+ task.getPosition());
//                    });
//                });
                return new ResponseEntity<>("Board updated", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("You do not participate in this board", HttpStatus.FORBIDDEN);
            }
        } else {
            System.out.println("board does not exists");
            return new ResponseEntity<>("Board does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/get")
    public ResponseEntity<Set<Board>> getUserBoards() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userService.findByUsername(authentication.getName());
        if (user.isPresent()) {
            System.out.println("user present");
            List<UserTeam> userTeams = userTeamService.findTeamsOfUser(user.get().getId());
            Set<Board> boards = new HashSet<>();
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


}
