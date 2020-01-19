package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.*;
import com.domko.kanbanbackendapp.payload.request.AddUserRequest;
import com.domko.kanbanbackendapp.repository.TeamRepository;
import com.domko.kanbanbackendapp.repository.UserRepository;
import com.domko.kanbanbackendapp.service.TeamService;
import org.hibernate.hql.internal.ast.ParseErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {

	@Autowired
	private TeamRepository teamRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserTeamServiceImpl userTeamService;
	@Autowired
	private PermissionService permissionService;

	public Team save(Team team) {
		return teamRepository.save(team);
	}

	public Optional<Team> findTeam(Long id) {
		return teamRepository.findById(id);
	}

	public ResponseEntity<List<Team>> getUserTeams() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<User> user = userRepository.findByUsername(authentication.getName());
		if (user.isPresent()) {
			List<UserTeam> userTeams = userTeamService.findTeamsOfUser(user.get().getId());
			List<Team> teams = userTeams.stream()
					.map(UserTeam::getTeam)
					.collect(Collectors.toList());
			return new ResponseEntity<>(teams, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}

	public ResponseEntity<List<UserTeam>> getTeamMembers(long teamId) {
		Optional<Team> team = teamRepository.findById(teamId);
		if (team.isPresent()) {
			if (permissionService.hasPermissionTo(team.get())) {
				List<UserTeam> userTeams = userTeamService.findUsersOfTeam(team.get().getId());
				return new ResponseEntity<>(userTeams, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	public ResponseEntity<Team> getTeamById(long teamId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<User> user = userRepository.findByUsername(authentication.getName());
		if (user.isPresent()) {
			Optional<Team> team = teamRepository.findById(teamId);
			return team.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	public ResponseEntity<String> getTeamMembers(AddUserRequest addUserRequest) {
		Optional<Team> team = teamRepository.findById(addUserRequest.getTeamId());
		Optional<User> invitedUser = userRepository.findByEmail(addUserRequest.getEmail());
		if (team.isPresent() && invitedUser.isPresent()) {
			if (permissionService.hasPermissionTo(team.get())) {
				userTeamService.addUserToTeam(invitedUser.get(), team.get(), TeamRole.MEMBER);
				return new ResponseEntity<>("User added to team", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
