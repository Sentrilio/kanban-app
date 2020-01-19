package com.domko.kanbanbackendapp.service;

import com.domko.kanbanbackendapp.model.Team;
import com.domko.kanbanbackendapp.model.TeamRole;
import com.domko.kanbanbackendapp.model.User;
import com.domko.kanbanbackendapp.model.UserTeam;

import java.util.List;

public interface UserTeamService {

    UserTeam addUserToTeam(User user, Team team, TeamRole role);
}
