package com.domko.kanbanbackendapp.service;

import com.domko.kanbanbackendapp.model.Team;
import com.domko.kanbanbackendapp.model.UserTeam;
import com.domko.kanbanbackendapp.payload.request.AddUserRequest;
import org.springframework.http.ResponseEntity;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface TeamService {

    ResponseEntity<List<Team>> getUserTeams();

    ResponseEntity<List<UserTeam>> getTeamMembers(long teamId);

    ResponseEntity<Team> getTeamById(long teamId);

    ResponseEntity<String> getTeamMembers(AddUserRequest addUserRequest);
}
