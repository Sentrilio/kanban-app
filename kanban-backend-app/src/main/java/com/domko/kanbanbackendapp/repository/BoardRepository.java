package com.domko.kanbanbackendapp.repository;

import com.domko.kanbanbackendapp.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

	@Override
	List<Board> findAll();

	@Override
	Optional<Board> findById(Long id);
}
