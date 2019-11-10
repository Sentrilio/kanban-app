package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.*;
import com.domko.kanbanbackendapp.service.TeamServiceImpl;
import com.domko.kanbanbackendapp.service.UserServiceImpl;
import com.domko.kanbanbackendapp.service.UserTeamServiceImpl;
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
	@Autowired
	private UserTeamServiceImpl userTeamService;

	@PostMapping(value = "/create")
	public Team createTeam(@RequestBody Team team) {
		return teamService.save(team);
	}

	@GetMapping(value = "/all")
	public List<Team> getTeams() {
		return teamService.findAll();
	}

	@PostMapping(value = "/add/{invitingUserId}/{teamId}/{invitedUserId}")
	public UserTeam addUserToTeam(@PathVariable Long invitingUserId, @PathVariable Long teamId, @PathVariable Long invitedUserId) {
		Optional<User> invitingUser = userService.findUser(invitingUserId);
		Optional<User> invitedUser = userService.findUser(invitedUserId);
		Optional<Team> team = teamService.findTeam(teamId);
		UserTeam userTeam = new UserTeam();
		if (invitingUser.isPresent() && team.isPresent() && invitedUser.isPresent()) {

			userTeam.setId(new UserTeamKey(invitedUserId,teamId));
			userTeam.setTeam(team.get());
			userTeam.setUser(invitedUser.get());
			userTeam.setTeamRole(TeamRole.LEADER);
			userTeamService.save(userTeam);
			System.out.println("Saving user in team in the future");
		}
		return userTeam;
	}


}
