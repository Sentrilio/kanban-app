package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.*;
import com.domko.kanbanbackendapp.payload.request.CreateTeamRequest;
import com.domko.kanbanbackendapp.service.implementation.UserTeamServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
//@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
@RequestMapping(value = "/api/userteam")
public class UserTeamController {

    private final UserTeamServiceImpl userTeamService;

    @Autowired
    public UserTeamController(UserTeamServiceImpl userTeamService) {
        this.userTeamService = userTeamService;
    }

    @PostMapping(value = "/create")
    @PreAuthorize("hasRole('USER')")
    public UserTeam createTeam(@RequestBody CreateTeamRequest createTeamRequest) {
        return userTeamService.createTeam(createTeamRequest);
    }
}
