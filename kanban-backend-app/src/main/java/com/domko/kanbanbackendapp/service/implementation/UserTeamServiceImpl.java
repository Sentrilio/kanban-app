package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.*;
import com.domko.kanbanbackendapp.repository.UserTeamRepository;
import com.domko.kanbanbackendapp.service.UserTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserTeamServiceImpl implements UserTeamService {

    @Autowired
    private UserTeamRepository userTeamRepository;

    public UserTeam save(UserTeam userTeam) {
        return userTeamRepository.save(userTeam);
    }

    public Optional<UserTeam> findById(UserTeamKey userTeamKey) {
        return userTeamRepository.findById(userTeamKey);
    }

    public List<UserTeam> findAll() {
        return userTeamRepository.findAll();
    }

    public List<UserTeam> findAll(Example<UserTeam> example) {
        return userTeamRepository.findAll(example);
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

}
