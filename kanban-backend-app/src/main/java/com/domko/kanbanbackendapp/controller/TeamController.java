package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.*;
import com.domko.kanbanbackendapp.payload.request.AddUserRequest;
import com.domko.kanbanbackendapp.service.implementation.*;
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
    private UserTeamServiceImpl userTeamService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private PermissionService permissionService;

    @GetMapping(value = "/all")
    public List<Team> getTeams() {
        return teamService.findAll();
    }

    @GetMapping(value = "/get")
    public ResponseEntity<List<Team>> getUserTeams() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userService.findByUsername(authentication.getName());
        if (user.isPresent()) {
            List<UserTeam> userTeams = userTeamService.findTeamsOfUser(user.get().getId());
            List<Team> teams = userTeams.stream()
                    .map(UserTeam::getTeam)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(teams, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/members/get/{teamId}")
    public ResponseEntity<List<UserTeam>> getTeamMembers(@PathVariable("teamId") long teamId) {
        Optional<Team> team = teamService.findTeam(teamId);
        if (team.isPresent()) {
            if (permissionService.hasPermissionToTeam(team.get())) {
                List<UserTeam> userTeams = userTeamService.findUsersOfTeam(team.get().getId());
                return new ResponseEntity<>(userTeams, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/get/{teamId}")
    public ResponseEntity<Team> getTeamById(@PathVariable("teamId") long teamId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userService.findByUsername(authentication.getName());
        if (user.isPresent()) {
            Optional<Team> team = teamService.findTeam(teamId);
            return team.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "/add/user")
    public ResponseEntity<String> getTeamMembers(@RequestBody AddUserRequest addUserRequest) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();// maybe to do add user if TeamRole.LEADER
//        Optional<User> user = userService.findByUsername(authentication.getName());
        Optional<Team> team = teamService.findTeam(addUserRequest.getTeamId());
        Optional<User> invitedUser = userService.findByEmail(addUserRequest.getEmail());
        if (team.isPresent() && invitedUser.isPresent()) {
            if (permissionService.hasPermissionToTeam(team.get())) {
                userTeamService.addUserToTeam(invitedUser.get(), team.get(), TeamRole.MEMBER);
                return new ResponseEntity<>("User added to team", HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
