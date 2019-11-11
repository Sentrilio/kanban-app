package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.model.Team;
import com.domko.kanbanbackendapp.service.implementation.BoardServiceImpl;
import com.domko.kanbanbackendapp.service.implementation.TeamServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/board")
public class BoardController {

	@Autowired
	private BoardServiceImpl boardService;
	@Autowired
	private TeamServiceImpl teamService;

	@GetMapping(value = "/all")
	public List<Board> getBoards() {
		return boardService.findAllBoards();
	}

	@PostMapping(value = "/create/{teamId}")
	public Board createBoard(@RequestBody Board board,@PathVariable Long teamId) {
		Optional<Team> team =teamService.findTeam(teamId);
		if (team.isPresent()) {
			team.get().addBoard(board);
			board.setTeam(team.get());
			teamService.save(team.get());
		}
		return boardService.saveBoard(board);
	}

	@GetMapping(value = "/get/{boardId}")
	public Board getBoard(@PathVariable Long boardId) {
		Optional<Board> board = boardService.findById(boardId);
		if (board.isPresent()) {
			board.get().getTasks()
					.forEach(value -> System.out.println(value.getDescription()));
			return board.get();
		}
		return null;
	}
}
