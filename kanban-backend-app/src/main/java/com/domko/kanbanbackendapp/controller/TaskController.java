package com.domko.kanbanbackendapp.controller;


import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.model.Task;
import com.domko.kanbanbackendapp.payload.request.CreateTaskRequest;
import com.domko.kanbanbackendapp.service.implementation.BoardServiceImpl;
import com.domko.kanbanbackendapp.service.implementation.TaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/api/task")
public class TaskController {

	@Autowired
	private TaskServiceImpl taskService;

	@Autowired
	private BoardServiceImpl boardService;

	@GetMapping(value = "/get/{id}")
	public Optional<Task> findById(@PathVariable Long id) {
		return taskService.findById(id);
	}

	@PostMapping(value = "/create")
	public Task createTask(@RequestBody CreateTaskRequest createTaskRequest) {
		Optional<Board> board = boardService.findBoard(createTaskRequest.getBoardId());
		if (board.isPresent()) {
			task.setBoard(board.get());
			return taskService.saveTask(task);
		} else {
			System.out.println("board not found");
			return null;
		}
	}
}
