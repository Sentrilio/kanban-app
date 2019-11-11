package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.*;
import com.domko.kanbanbackendapp.service.UserService;
import com.domko.kanbanbackendapp.service.implementation.TeamServiceImpl;
import com.domko.kanbanbackendapp.service.implementation.UserServiceImpl;
import com.domko.kanbanbackendapp.service.implementation.UserTeamServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;

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

	@PostMapping(value = "/add/{invitingUserId}/{invitedUserId}/{teamId}")
	public UserTeam addUserToTeam(@PathVariable Long invitingUserId, @PathVariable Long teamId, @PathVariable Long invitedUserId) {
		Optional<User> invitingUser = userService.findUser(invitingUserId);
		Optional<User> invitedUser = userService.findUser(invitedUserId);
		Optional<Team> team = teamService.findTeam(teamId);
		UserTeam userTeam = null;
		if (invitingUser.isPresent() && team.isPresent() && invitedUser.isPresent()) {
			UserTeamKey userTeamKey = new UserTeamKey(invitedUserId, teamId);
			if (userTeamService.findById(userTeamKey).isPresent()) {
				System.out.println("Użytkownik jest już w drużynie");
			} else {
				System.out.println("Użytkownik ten nie jest jeszcze w tej drużynie");
				userTeam = new UserTeam(userTeamKey,invitedUser.get(),team.get(), TeamRole.MEMBER);
				userTeamService.save(userTeam);
			}

		}
		return userTeam;
	}
	@PostMapping(value = "/create/{teamName}/{userId}")
	public UserTeam createTeam(@PathVariable String teamName, @PathVariable Long userId) {
		Optional<User> user = userService.findUser(userId);
		if (user.isPresent()) {
			Team team = teamService.save(new Team(teamName));
			return userTeamService.createTeam(user.get(),team);
		} else {
			return null;
		}
	}
}
