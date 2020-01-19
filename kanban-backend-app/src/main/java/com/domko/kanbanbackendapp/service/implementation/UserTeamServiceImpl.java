package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.*;
import com.domko.kanbanbackendapp.payload.request.CreateTeamRequest;
import com.domko.kanbanbackendapp.repository.TeamRepository;
import com.domko.kanbanbackendapp.repository.UserRepository;
import com.domko.kanbanbackendapp.repository.UserTeamRepository;
import com.domko.kanbanbackendapp.service.UserTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserTeamServiceImpl implements UserTeamService {

    private final UserTeamRepository userTeamRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public UserTeamServiceImpl(UserTeamRepository userTeamRepository, UserRepository userRepository,
                               TeamRepository teamRepository) {
        this.userTeamRepository = userTeamRepository;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
    }

    public UserTeam addUserToTeam(User user, Team team, TeamRole role) {
        return userTeamRepository.save(new UserTeam(new UserTeamKey(user.getId(), team.getId()), user, team, role));
    }

    public UserTeam createTeam(CreateTeamRequest createTeamRequest) {
        String teamName = createTeamRequest.getTeamName();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            Optional<User> user = userRepository.findByUsername(username);
            if (user.isPresent()) {
                Team team = teamRepository.save(new Team(teamName));
                addUserToTeam(user.get(), team, TeamRole.LEADER);
                return null;
            } else {
                return null;
            }
        } else {
            System.out.println("authentication is instance of anonymouauthenticationtoken");
            return null;
        }
    }
}
