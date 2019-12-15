package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.Team;
import com.domko.kanbanbackendapp.service.implementation.TeamServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/api/team")
public class TeamController {

    @Autowired
    private TeamServiceImpl teamService;

    @GetMapping(value = "/all")
    public List<Team> getTeams() {
        return teamService.findAll();
    }

}
