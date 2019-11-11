package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.*;
import com.domko.kanbanbackendapp.service.implementation.TeamServiceImpl;
import com.domko.kanbanbackendapp.service.implementation.UserServiceImpl;
import com.domko.kanbanbackendapp.service.implementation.UserTeamServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/user-team")
public class UserTeamController {

	@Autowired
	private UserTeamServiceImpl userTeamService;
	@Autowired
	private UserServiceImpl userService;
	@Autowired
	private TeamServiceImpl teamService;

	@GetMapping("/all")
	public List<UserTeam> getAllUserTeams() {
		return userTeamService.findAll();
	}

//	@GetMapping("/get/{userId}")
//	public List<UserTeam> getTeamsOfUserById(@PathVariable Long userId) {
//		UserTeam userTeam = new UserTeam();
//		User user = new User();
//		user.setUserId(userId);
//		userTeam.setUser(user);
//		Example<UserTeam> example = Example.of(userTeam);
//		return userTeamService.findAll(example);
//	}

	@GetMapping("/get/{email}")
	public List<UserTeam> getTeamsOfUserByEmail(@PathVariable String email) {
		UserTeam userTeam = new UserTeam();
		User user = new User();
		user.setEmail(email);
		userTeam.setUser(user);
		Example<UserTeam> example = Example.of(userTeam);
		return userTeamService.findAll(example);
	}
	@GetMapping("/getTeams/{email}")
	public List<Team> getTeamsOfUserByEmail1(@PathVariable String email) {
		UserTeam userTeam = new UserTeam();
		User user = new User();
		user.setEmail(email);
		userTeam.setUser(user);
		Example<UserTeam> example = Example.of(userTeam);
		List<UserTeam> userTeams = userTeamService.findAll(example);
		List<Team> teams = new ArrayList<>();
		userTeams.stream().forEach(value-> teams.add(value.getTeam()));
		return teams;
	}

	@PostMapping(value = "/add/{invitingUserId}/{invitedUserId}/{teamId}")
	public UserTeam addUserToTeam(@PathVariable Long invitingUserId, @PathVariable Long invitedUserId, @PathVariable Long teamId) {
		Optional<User> invitingUser = userService.findUser(invitingUserId);
		Optional<User> invitedUser = userService.findUser(invitedUserId);
		Optional<Team> team = teamService.findTeam(teamId);
		if (invitingUser.isPresent() && team.isPresent() && invitedUser.isPresent()) {
			UserTeamKey userTeamKey = new UserTeamKey(invitedUserId, teamId);
			if (userTeamService.findById(userTeamKey).isPresent()) {
				System.out.println("Użytkownik jest już w drużynie");
			} else {
				System.out.println("Użytkownik ten nie jest jeszcze w tej drużynie");
				return userTeamService.addUserToTeam(invitedUser.get(), team.get(), TeamRole.MEMBER);
//				userTeam = new UserTeam(userTeamKey,invitedUser.get(),team.get(), TeamRole.MEMBER);
//				userTeamService.save(userTeam);
			}

		}
		return null;
	}

	@PostMapping(value = "/create/{teamName}/{userId}")
	public UserTeam createTeam(@PathVariable String teamName, @PathVariable Long userId) {
		Optional<User> user = userService.findUser(userId);
		if (user.isPresent()) {
			Team team = teamService.save(new Team(teamName));
			return userTeamService.addUserToTeam(user.get(), team, TeamRole.LEADER);
		} else {
			return null;
		}
	}
}