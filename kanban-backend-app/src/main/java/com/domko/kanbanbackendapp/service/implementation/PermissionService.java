package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {

    @Autowired
    private UserTeamServiceImpl userTeamService;

    public boolean hasPermissionToTask(Task task) {
        return hasPermissionToBoardList(task.getColumn());
    }

    public boolean hasPermissionToBoardList(BColumn BColumn) {
        return hasPermissionToBoard(BColumn.getBoard());
    }

    public boolean hasPermissionToBoard(Board board) {
        return hasPermissionToTeam(board.getTeam());
    }

    public boolean hasPermissionToTeam(Team team) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<UserTeam> userTeams = userTeamService.findUsersOfTeam(team.getId());
        return userTeams
                .stream()
                .anyMatch(e -> e.getUser().getUsername().equals(authentication.getName()));
    }
}
