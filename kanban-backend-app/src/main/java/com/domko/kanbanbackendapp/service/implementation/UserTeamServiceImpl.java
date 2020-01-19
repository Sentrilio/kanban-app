package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.*;
import com.domko.kanbanbackendapp.payload.request.CreateTeamRequest;
import com.domko.kanbanbackendapp.repository.TeamRepository;
import com.domko.kanbanbackendapp.repository.UserRepository;
import com.domko.kanbanbackendapp.repository.UserTeamRepository;
import com.domko.kanbanbackendapp.service.UserTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserTeamServiceImpl implements UserTeamService {

    @Autowired
    private UserTeamRepository userTeamRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;

    public UserTeam save(UserTeam userTeam) {
        return userTeamRepository.save(userTeam);
    }

    public Optional<UserTeam> findById(UserTeamKey userTeamKey) {
        return userTeamRepository.findById(userTeamKey);
    }

    @Override
    public List<UserTeam> findTeamsOfUser(Long userId) {
        return userTeamRepository.findAllTeamsOfUser(userId);
    }

    @Override
    public UserTeam addUserToTeam(User user, Team team, TeamRole role) {
        return save(new UserTeam(new UserTeamKey(user.getId(), team.getId()), user, team, role));
    }

    @Override
    public List<UserTeam> findUsersOfTeam(Long teamId) {
        return userTeamRepository.findAllById_TeamId(teamId);
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
