package com.domko.kanbanbackendapp.repository;

import com.domko.kanbanbackendapp.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Override
    List<Board> findAll();

    @Query(value = "SELECT id FROM board", nativeQuery = true)
    List<Long> findAllIds();

    @Override
    Optional<Board> findById(Long id);

//	List<Board> findAllById(List<Long> idList);
}
