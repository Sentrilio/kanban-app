package com.domko.kanbanbackendapp.service;

import com.domko.kanbanbackendapp.model.Team;
import com.domko.kanbanbackendapp.model.TeamRole;
import com.domko.kanbanbackendapp.model.User;
import com.domko.kanbanbackendapp.model.UserTeam;

import java.util.List;

public interface UserTeamService {

	List<UserTeam> findTeamsOfUser(Long userId);

	UserTeam addUserToTeam(User user, Team team, TeamRole role);

	List<UserTeam> findUsersOfTeam(Long teamId);
}
