package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.*;
import com.domko.kanbanbackendapp.repository.UserTeamRepository;
import com.domko.kanbanbackendapp.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final UserTeamRepository userTeamRepository;

    @Autowired
    public PermissionServiceImpl(UserTeamRepository userTeamRepository) {
        this.userTeamRepository = userTeamRepository;
    }

    @Override
    public boolean hasPermissionTo(Task task) {
        return hasPermissionTo(task.getColumn());
    }

    @Override
    public boolean hasPermissionTo(BColumn BColumn) {
        return hasPermissionTo(BColumn.getBoard());
    }

    @Override
    public boolean hasPermissionTo(Board board) {
        return hasPermissionTo(board.getTeam());
    }

    @Override
    public boolean hasPermissionTo(Team team) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<UserTeam> userTeams = userTeamRepository.findAllById_TeamId(team.getId());
        return userTeams
                .stream()
                .anyMatch(e -> e.getUser().getUsername().equals(authentication.getName()));
    }
}
