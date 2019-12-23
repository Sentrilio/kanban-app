package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.model.Team;
import com.domko.kanbanbackendapp.model.User;
import com.domko.kanbanbackendapp.model.UserTeam;
import com.domko.kanbanbackendapp.payload.request.CreateBoardRequest;
import com.domko.kanbanbackendapp.security.jwt.JwtUtils;
import com.domko.kanbanbackendapp.service.implementation.BoardServiceImpl;
import com.domko.kanbanbackendapp.service.implementation.TeamServiceImpl;
import com.domko.kanbanbackendapp.service.implementation.UserServiceImpl;
import com.domko.kanbanbackendapp.service.implementation.UserTeamServiceImpl;
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
    private JwtUtils jwtUtils;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserTeamServiceImpl userTeamService;

    @GetMapping(value = "/all")
    public List<Board> getAllBoards() {
        return boardService.findAllBoards();
    }

    @PostMapping(value = "/create")
    public ResponseEntity<String> createBoard(@RequestBody CreateBoardRequest createBoardRequest) {
        Optional<Team> team = teamService.findTeam(createBoardRequest.getTeamId());
        if (team.isPresent()) {
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
            return new ResponseEntity<>("Team does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
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

//    @GetMapping(value = "/get/{boardId}")
//    public ResponseEntity<Board> getBoard(@PathVariable Long boardId) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Optional<Board> board = boardService.findBoard(boardId);
//        if (board.isPresent()) {
//            List<UserTeam> userTeams = userTeamService.findUsersOfTeam(board.get().getTeam().getTeamId());
//            if (!userTeams.isEmpty()) {
//                System.out.println("Users of team found");
//                if (userTeamService.hasPermission(userTeams)) {
//                    board.get().getTasks()
//                            .forEach(value -> System.out.println(value.getDescription()));
//                    return new ResponseEntity<>(board.get(), HttpStatus.OK);
//                } else {
//                    return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
//                }
//            }
//        }
//        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//    }


}
