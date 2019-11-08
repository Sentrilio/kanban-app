package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.Team;
import com.domko.kanbanbackendapp.model.TeamRole;
import com.domko.kanbanbackendapp.model.User;
import com.domko.kanbanbackendapp.model.UserTeam;
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

	@PostMapping(value = "/add/{invitingUserId}/{teamId}/{invitedUserEmail}")
	public Optional<Team> addUserToTeam(@PathVariable Long invitingUserId,@PathVariable Long teamId, @PathVariable String invitedUserEmail) {
		Optional<User> invitingUser = userService.findUser(invitingUserId);
		Optional<User> invitedUser = userService.findByEmail(invitedUserEmail);
		Optional<Team> team = teamService.findTeam(teamId);

		if (invitingUser.isPresent() && team.isPresent() && invitedUser.isPresent()) {
			UserTeam userTeam = new UserTeam();
			userTeam.setTeam(team.get());
			userTeam.setUser(invitedUser.get());
			userTeam.setTeamRole(TeamRole.LEADER);
//			team.get().getUsers().add()
			System.out.println(userTeam.getTeamRole());
			teamService.save(team.get());

		}
		return team;
	}


}
