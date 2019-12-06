package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.*;
import com.domko.kanbanbackendapp.security.jwt.JwtUtils;
import com.domko.kanbanbackendapp.service.implementation.TeamServiceImpl;
import com.domko.kanbanbackendapp.service.implementation.UserServiceImpl;
import com.domko.kanbanbackendapp.service.implementation.UserTeamServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
//@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
@RequestMapping(value = "/api/userteam")
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


//    @GetMapping(value = "/get")
//    public List<UserTeam> getUserTeams(@RequestHeader("authorization") String headerAuth) {
//        String username = jwtUtils.getUserNameFromToken(headerAuth);
//        Optional<User> user = userService.findByUsername(username);
//        if (user.isPresent()) {
//            UserTeam userTeam = new UserTeam();
//            userTeam.setUser(user.get());
//            Example<UserTeam> example = Example.of(userTeam);
//            List<UserTeam> teams = userTeamService.findAll(example);
//            System.out.println("Teams");
//            if (teams.isEmpty()) {
//                System.out.println("No Teams");
//            } else {
//                teams.forEach(e -> System.out.println(e.getTeam().getBoards()));
//            }
//            return userTeamService.findAll(example);
//        } else {
//            System.out.println("teams not found");
//            return new ArrayList<>();
//        }
//    }
//	@GetMapping("/get/{email}")
//	public List<UserTeam> getTeamsOfUserByEmail(@PathVariable String email) {
//		UserTeam userTeam = new UserTeam();
//		User user = new User();
//		user.setEmail(email);
//		userTeam.setUser(user);
//		Example<UserTeam> example = Example.of(userTeam);
//		return userTeamService.findAll(example);
//	}

//	@GetMapping("/getTeams/{email}")
//	public List<Team> getTeamsOfUserByEmail1(@PathVariable String email) {
//		UserTeam userTeam = new UserTeam();
//		User user = new User();
//		user.setEmail(email);
//		userTeam.setUser(user);
//		Example<UserTeam> example = Example.of(userTeam);
//		List<UserTeam> userTeams = userTeamService.findAll(example);
//		List<Team> teams = new ArrayList<>();
//		userTeams.forEach(value-> teams.add(value.getTeam()));
//		return teams;
//	}

    @RequestMapping(value = "/add/{invitingUserId}/{invitedUserId}/{teamId}", method = RequestMethod.POST)
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

    @PostMapping(value = "/create", consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasRole('USER')")
    public UserTeam createTeam(@RequestBody String teamName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            System.out.println(username);
            Optional<User> user = userService.findByUsername(username);
            if (user.isPresent()) {
                System.out.println("user present");
                System.out.println(username);
                Team team = teamService.save(new Team(teamName));
                return userTeamService.addUserToTeam(user.get(), team, TeamRole.LEADER);
            } else {
                return null;
            }
        } else {
            System.out.println("authentication is instance of anonymouauthenticationtoken");
            return null;
        }
    }
}
