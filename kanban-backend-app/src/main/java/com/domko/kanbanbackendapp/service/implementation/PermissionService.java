package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.*;
import com.domko.kanbanbackendapp.repository.UserTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {

    private final UserTeamRepository userTeamRepository;

    @Autowired
    public PermissionService(UserTeamRepository userTeamRepository) {
        this.userTeamRepository = userTeamRepository;
    }

    public boolean hasPermissionTo(Task task) {
        return hasPermissionTo(task.getColumn());
    }

    public boolean hasPermissionTo(BColumn BColumn) {
        return hasPermissionTo(BColumn.getBoard());
    }

    public boolean hasPermissionTo(Board board) {
        return hasPermissionTo(board.getTeam());
    }

    public boolean hasPermissionTo(Team team) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<UserTeam> userTeams = userTeamRepository.findAllById_TeamId(team.getId());
        return userTeams
                .stream()
                .anyMatch(e -> e.getUser().getUsername().equals(authentication.getName()));
    }
}
