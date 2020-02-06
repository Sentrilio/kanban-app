package com.domko.kanbanbackendapp.service;

import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.model.Team;
import com.domko.kanbanbackendapp.payload.request.CreateBoardRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BoardService {

    ResponseEntity<?> createBoard(CreateBoardRequest createBoardRequest);

    Board createBoard(CreateBoardRequest createBoardRequest, Team team);

    void createBoardStatisticsForTodayAndTomorrow(Board board);

    ResponseEntity<List<Board>> getUserBoards();

    ResponseEntity<Board> getBoardById(long boardId);

    int getNumberOfTasks(Board board);
}
