package com.domko.kanbanbackendapp.service;

import com.domko.kanbanbackendapp.model.Board;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BoardService {

	List<Board> findAllBoards();

	Optional<Board> findBoard(Long id);

	public Board save(Board board);
}
