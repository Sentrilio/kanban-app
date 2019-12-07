package com.domko.kanbanbackendapp.controller;

import antlr.StringUtils;
import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.model.Team;
import com.domko.kanbanbackendapp.security.jwt.JwtUtils;
import com.domko.kanbanbackendapp.service.implementation.BoardServiceImpl;
import com.domko.kanbanbackendapp.service.implementation.TeamServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/board")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BoardController {

    @Autowired
    private BoardServiceImpl boardService;
    @Autowired
    private TeamServiceImpl teamService;
    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping(value = "/all")
    public List<Board> getBoards() {
        return boardService.findAllBoards();
    }

    @GetMapping(value = "/user-boards")
    public List<Board> getUserBoards(@RequestHeader("authorization") String jwt){
//        System.out.println(jwt);
//        jwt = jwt.replaceAll("[\\x00-\\x09\\x11\\x12\\x14-\\x1F\\x7F]", "");
//		System.out.println(jwt);
//        System.out.println(username);
//        boardService.findAllBoards().forEach(e -> {
//            System.out.println(e.getName());
//        });
        return boardService.findAllBoards();
    }

    @PostMapping(value = "/create/{teamId}")
    public Board createBoard(@RequestBody Board board, @PathVariable Long teamId) {
        Optional<Team> team = teamService.findTeam(teamId);
        if (team.isPresent()) {
            team.get().addBoard(board);
            board.setTeam(team.get());
            teamService.save(team.get());
        }
        return boardService.saveBoard(board);
    }

    @GetMapping(value = "/get/{boardId}")
    public Board getBoard(@PathVariable Long boardId) {
        Optional<Board> board = boardService.findById(boardId);
        if (board.isPresent()) {
            board.get().getTasks()
                    .forEach(value -> System.out.println(value.getDescription()));
            return board.get();
        }
        return null;
    }
}
