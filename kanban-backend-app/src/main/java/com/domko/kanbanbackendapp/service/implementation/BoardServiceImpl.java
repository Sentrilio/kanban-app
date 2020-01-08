package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.repository.BoardRepository;
import com.domko.kanbanbackendapp.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardRepository boardRepository;

	@Override
	public List<Board> findAllBoards() {
		return boardRepository.findAll();
	}

	@Override
	public Board save(Board board) {
		return boardRepository.save(board);
	}

	public Optional<Board> findBoard(Long id){
		return boardRepository.findById(id);
	}
}
