package com.domko.kanbanbackendapp.service;

import com.domko.kanbanbackendapp.model.Team;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface TeamService {

	Team save(Team team);
	Optional<Team> findTeam(Long id);
	List<Team> findAll();

}
