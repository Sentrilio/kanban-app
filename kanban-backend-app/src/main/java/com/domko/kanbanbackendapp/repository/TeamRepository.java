package com.domko.kanbanbackendapp.repository;

import com.domko.kanbanbackendapp.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

	@Override
	Optional<Team> findById(Long id);

	@Override
	List<Team> findAll();

	@Override
	boolean existsById(Long id);

}
