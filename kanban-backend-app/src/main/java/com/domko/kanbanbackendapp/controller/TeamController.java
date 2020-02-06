package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.*;
import com.domko.kanbanbackendapp.payload.request.AddUserRequest;
import com.domko.kanbanbackendapp.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/api/team")
public class TeamController {

    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping(value = "/get")
    public ResponseEntity<List<Team>> getUserTeams() {
        return teamService.getUserTeams();
    }

    @GetMapping(value = "/members/get/{teamId}")
    public ResponseEntity<List<UserTeam>> getTeamMembers(@PathVariable("teamId") long teamId) {
        return teamService.getTeamMembers(teamId);
    }

    @GetMapping(value = "/get/{teamId}")
    public ResponseEntity<Team> getTeamById(@PathVariable("teamId") long teamId) {
        return teamService.getTeamById(teamId);
    }

    @PostMapping(value = "/add/user")
    public ResponseEntity<String> getTeamMembers(@RequestBody AddUserRequest addUserRequest) {
        return teamService.getTeamMembers(addUserRequest);
    }
}
