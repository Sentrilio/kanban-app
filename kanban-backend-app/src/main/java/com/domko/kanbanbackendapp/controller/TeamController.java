package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.model.Team;
import com.domko.kanbanbackendapp.model.User;
import com.domko.kanbanbackendapp.model.UserTeam;
import com.domko.kanbanbackendapp.service.TeamService;
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
import java.util.stream.Collectors;

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
    @Autowired
    private UserServiceImpl userService;

    @GetMapping(value = "/all")
    public List<Team> getTeams() {
        return teamService.findAll();
    }

    @GetMapping(value = "/get")
    public ResponseEntity<List<Team>> getUserTeams() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userService.findByUsername(authentication.getName());
        if (user.isPresent()) {
            List<UserTeam> userTeams = userTeamService.findTeamsOfUser(user.get().getUserId());
            List<Team> teams = userTeams.stream()
                    .map(UserTeam::getTeam)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(teams, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/get/{teamId}")
    public ResponseEntity<Team> getTeamById(@PathVariable("teamId") long teamId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userService.findByUsername(authentication.getName());
        if (user.isPresent()) {
            Optional<Team> team = teamService.findTeam(teamId);
            if (team.isPresent()) {
                return new ResponseEntity<>(team.get(), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
