package com.domko.kanbanbackendapp.service;

import com.domko.kanbanbackendapp.model.Team;
import com.domko.kanbanbackendapp.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

}
