package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.*;
import com.domko.kanbanbackendapp.payload.request.CreateTeamRequest;
import com.domko.kanbanbackendapp.service.implementation.TeamServiceImpl;
import com.domko.kanbanbackendapp.service.implementation.UserServiceImpl;
import com.domko.kanbanbackendapp.service.implementation.UserTeamServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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



//    @GetMapping("/get")
//    public List<Team> getUserTeams() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Optional<User> user = userService.findByUsername(authentication.getName());
//        if (user.isPresent()) {
//            List<UserTeam> userTeams = userTeamService.findTeamsOfUser(user.get().getId());
//            List<Team> teams = userTeams.stream()
//                    .map(UserTeam::getTeam)
//                    .collect(Collectors.toList());
//            return teams;
//        } else {
//            return null;
//        }
//    }


    @PostMapping(value = "/create")
    @PreAuthorize("hasRole('USER')")
    public UserTeam createTeam(@RequestBody CreateTeamRequest createTeamRequest) {
        String teamName = createTeamRequest.getTeamName();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            Optional<User> user = userService.findByUsername(username);
            if (user.isPresent()) {
                Team team = teamService.save(new Team(teamName));
                userTeamService.addUserToTeam(user.get(), team, TeamRole.LEADER);
                return null;
            } else {
                return null;
            }
        } else {
            System.out.println("authentication is instance of anonymouauthenticationtoken");
            return null;
        }
    }

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
            }
        }
        return null;
    }
}
