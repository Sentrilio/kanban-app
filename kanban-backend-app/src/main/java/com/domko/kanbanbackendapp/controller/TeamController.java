package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.Team;
import com.domko.kanbanbackendapp.model.TeamRole;
import com.domko.kanbanbackendapp.model.User;
import com.domko.kanbanbackendapp.model.UserTeam;
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

	@PostMapping(value = "/add/{invitingUserId}/{teamId}/{invitedUserEmail}", consumes = "application/json;charset=UTF-8")
	public UserTeam addUserToTeam(@PathVariable String invitingUserId, @PathVariable String teamId, @PathVariable String invitedUserEmail) {
		Optional<User> invitingUser = userService.findUser(Long.parseLong(invitingUserId));
		Optional<User> invitedUser = userService.findByEmail(invitedUserEmail);
		Optional<Team> team = teamService.findTeam(Long.parseLong(teamId));
		UserTeam userTeam = new UserTeam();
		if (invitingUser.isPresent() && team.isPresent() && invitedUser.isPresent()) {
			team.get().addUser(invitedUser.get());
			userService.save(invitedUser.get());
			teamService.save(team.get());
			//			userTeam.setUser(invitedUser.get());
//			userTeam.setTeam(team.get());
//			userTeam.setTeamRole(TeamRole.LEADER);
//			System.out.println(userTeam.getTeamRole().toString());
//			try {
//				userTeamService.save(userTeam);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}

//			userTeam.setTeam(team.get());
//			userTeam.setUser(invitedUser.get());
//			userTeam.setTeamRole(TeamRole.LEADER);
////			team.get().getUsers().add()
//			System.out.println(userTeam.getTeamRole());
//			teamService.save(team.get());

		}
		return userTeam;
	}


}
