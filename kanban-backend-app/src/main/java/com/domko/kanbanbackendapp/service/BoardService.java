package com.domko.kanbanbackendapp.service;

import com.domko.kanbanbackendapp.model.Board;

import java.util.List;
import java.util.Set;

public interface BoardService {

	List<Board> findAllBoards();
}
