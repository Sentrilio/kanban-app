package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.model.Team;
import com.domko.kanbanbackendapp.payload.request.CreateBoardRequest;
import com.domko.kanbanbackendapp.repository.BoardRepository;
import com.domko.kanbanbackendapp.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public Board createBoard(CreateBoardRequest createBoardRequest, Team team) {
		Board board = new Board();
		board.setName(createBoardRequest.getBoardName());
		board.setTeam(team);
//		if (createBoardRequest.getWipLimit() == null) {
//			board.setWipLimit(5);
//		} else {
//			board.setWipLimit(createBoardRequest.getWipLimit());
//		}
		return save(board);
	}
}
