package com.domko.kanbanbackendapp.controller;


import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.model.Task;
import com.domko.kanbanbackendapp.service.implementation.BoardServiceImpl;
import com.domko.kanbanbackendapp.service.implementation.TaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/task")
public class TaskController {

	@Autowired
	private TaskServiceImpl taskService;

	@Autowired
	private BoardServiceImpl boardService;

	@GetMapping(value = "/get/{id}")
	public Optional<Task> findById(@PathVariable Long id) {
		return taskService.findById(id);
	}

	@PostMapping(value = "/create/{boardId}")
	public Task createTask(@RequestBody Task task, @PathVariable Long boardId) {
		Optional<Board> board = boardService.findById(boardId);
		if (board.isPresent()) {
			task.setBoard(board.get());
			return taskService.saveTask(task);
		} else {
			System.out.println("board not found");
			return null;
		}
	}
}
