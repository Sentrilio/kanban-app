package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.Team;
import com.domko.kanbanbackendapp.model.User;
import com.domko.kanbanbackendapp.service.TeamServiceImpl;
import com.domko.kanbanbackendapp.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/team")
public class TeamController {

	@Autowired
	private TeamServiceImpl teamService;
	@Autowired
	private UserServiceImpl userService;

	@PostMapping(value = "/create")
	public Team createTeam(@RequestBody Team team) {
		return teamService.save(team);
	}

	@GetMapping(value = "/all")
	public List<Team> getTeams() {
		return teamService.findAll();
	}

	@PostMapping(value = "/add/{teamId}/{userId}")
	public Optional<Team> addUserToTeam(@PathVariable Long teamId, @PathVariable Long userId) {
		Optional<User> user = userService.findUser(userId);
		Optional<Team> team = teamService.findTeam(teamId);
		if (user.isPresent() && team.isPresent()) {
			team.get().addUser(user.get());
			teamService.save(team.get());
		}
		return team;
	}

}
