package com.domko.kanbanbackendapp.controller;


import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.model.BList;
import com.domko.kanbanbackendapp.model.Task;
import com.domko.kanbanbackendapp.payload.request.CreateTaskRequest;
import com.domko.kanbanbackendapp.service.implementation.BoardServiceImpl;
import com.domko.kanbanbackendapp.service.implementation.ListServiceImpl;
import com.domko.kanbanbackendapp.service.implementation.TaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private ListServiceImpl listService;

    @GetMapping(value = "/get/{id}")
    public Optional<Task> findById(@PathVariable Long id) {
        return taskService.findById(id);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<String> createTask(@RequestBody CreateTaskRequest createTaskRequest) {
        Optional<Board> board = boardService.findBoard(createTaskRequest.getBoardId());
        Optional<BList> list = listService.findList(createTaskRequest.getListId());
        if (board.isPresent() && list.isPresent()) {
            Task task = new Task();
            task.setDescription(createTaskRequest.getDescription());
            task.setContent(createTaskRequest.getContent());
//            task.setBoard(board.get());
            task.setBList(list.get());
            taskService.saveTask(task);
            return new ResponseEntity<>("Task created", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Board or list does not exists", HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
}
