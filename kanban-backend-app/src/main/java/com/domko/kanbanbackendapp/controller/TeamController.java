package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.model.Team;
import com.domko.kanbanbackendapp.model.UserTeam;
import com.domko.kanbanbackendapp.service.implementation.BoardServiceImpl;
import com.domko.kanbanbackendapp.service.implementation.TeamServiceImpl;
import com.domko.kanbanbackendapp.service.implementation.UserTeamServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/api/team")
public class TeamController {

    @Autowired
    private TeamServiceImpl teamService;
    @Autowired
    private BoardServiceImpl boardService;
    @Autowired
    private UserTeamServiceImpl userTeamService;

    @GetMapping(value = "/all")
    public List<Team> getTeams() {
        return teamService.findAll();
    }

    @GetMapping(value = "/getByBoardId/{boardId}")
    public ResponseEntity<Team> getTeamByBoardId(@PathVariable Long boardId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Board> board = boardService.findBoard(boardId);
        if (board.isPresent()) {

            List<UserTeam> userTeams = userTeamService.findUsersOfTeam(board.get().getTeam().getTeamId());
//            if (!userTeams.isEmpty()) {
//            System.out.println("userteam is not empty");
//                boolean hasPermissions = userTeams
//                        .stream()
//                        .anyMatch(e -> e.getUser().getUsername().equals(authentication.getName()));
            if (userTeamService.hasPermission(userTeams)) {
                System.out.println("has permission");
                Optional<Team> team = teamService.findTeam(board.get().getTeam().getTeamId());
                if (team.isPresent()) {
                    System.out.println("team found");
                    return new ResponseEntity<>(team.get(), HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }
//        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}
