package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.*;
import com.domko.kanbanbackendapp.service.implementation.TeamServiceImpl;
import com.domko.kanbanbackendapp.service.implementation.UserServiceImpl;
import com.domko.kanbanbackendapp.service.implementation.UserTeamServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/team")
public class TeamController {

	@Autowired
	private TeamServiceImpl teamService;
	@Autowired
	private UserServiceImpl userService;
	@Autowired
	private UserTeamServiceImpl userTeamService;

	@GetMapping(value = "/all")
	public List<Team> getTeams() {
		return teamService.findAll();
	}

}
