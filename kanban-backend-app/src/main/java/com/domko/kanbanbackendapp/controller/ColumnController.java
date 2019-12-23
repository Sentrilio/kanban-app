package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.model.BColumn;
import com.domko.kanbanbackendapp.model.UserTeam;
import com.domko.kanbanbackendapp.payload.request.CreateColumnRequest;
import com.domko.kanbanbackendapp.service.implementation.BoardServiceImpl;
import com.domko.kanbanbackendapp.service.implementation.ColumnServiceImpl;
import com.domko.kanbanbackendapp.service.implementation.UserTeamServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/column")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ColumnController {

    @Autowired
    private BoardServiceImpl boardService;
    @Autowired
    private ColumnServiceImpl columnService;
    @Autowired
    private UserTeamServiceImpl userTeamService;

    @PostMapping(value = "/create")
    public ResponseEntity<String> createColumn(@RequestBody CreateColumnRequest createColumnRequest) {
        Optional<Board> board = boardService.findBoard(createColumnRequest.getBoardId());
        if (board.isPresent()) {
            List<UserTeam> userTeams = userTeamService.findUsersOfTeam(board.get().getTeam().getId());
            if (userTeamService.hasPermission(userTeams)) {
                BColumn column = new BColumn();
                column.setName(createColumnRequest.getColumnName());
                column.setBoard(board.get());
                column.setPosition(board.get().getColumns().size() + 1);
                System.out.println("column name: " + column.getName());
                columnService.save(column);
                return new ResponseEntity<>("Column created", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }

        } else {
            return new ResponseEntity<>("Board does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
