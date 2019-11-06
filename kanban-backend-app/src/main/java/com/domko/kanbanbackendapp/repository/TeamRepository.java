package com.domko.kanbanbackendapp.repository;

import com.domko.kanbanbackendapp.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

	@Override
	Optional<Team> findById(Long aLong);

	@Override
	List<Team> findAll();
}
