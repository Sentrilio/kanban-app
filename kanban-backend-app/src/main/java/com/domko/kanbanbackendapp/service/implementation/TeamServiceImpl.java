package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.*;
import com.domko.kanbanbackendapp.repository.TeamRepository;
import com.domko.kanbanbackendapp.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TeamServiceImpl implements TeamService {

	@Autowired
	private TeamRepository teamRepository;

	@Override
	public Team save(Team team) {
		return teamRepository.save(team);
	}

	public Optional<Team> findTeam(Long id) {
		return teamRepository.findById(id);
	}

	@Override
	public List<Team> findAll() {
		return teamRepository.findAll();
	}

	public Team createTeam(User user, String teamName) {
		Team team = new Team(teamName);
		Set<UserTeam> users = Set.of(new UserTeam(new UserTeamKey(user.getUserId(), team.getTeamId()), user, team, TeamRole.LEADER));
		team.setUserTeams(users);
		return save(team);
	}
}
