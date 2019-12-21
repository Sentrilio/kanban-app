package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.model.BList;
import com.domko.kanbanbackendapp.model.UserTeam;
import com.domko.kanbanbackendapp.payload.request.CreateListRequest;
import com.domko.kanbanbackendapp.service.implementation.BoardServiceImpl;
import com.domko.kanbanbackendapp.service.implementation.ListServiceImpl;
import com.domko.kanbanbackendapp.service.implementation.UserTeamServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/list")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ListController {

    @Autowired
    private BoardServiceImpl boardService;

    @Autowired
    private ListServiceImpl listService;
    @Autowired
    private UserTeamServiceImpl userTeamService;

    @PostMapping(value = "/create")
    public ResponseEntity<String> createList(@RequestBody CreateListRequest createBoardRequest) {
        Optional<Board> board = boardService.findBoard(createBoardRequest.getBoardId());
        if (board.isPresent()) {
            List<UserTeam> userTeams = userTeamService.findUsersOfTeam(board.get().getTeam().getTeamId());
            if (userTeamService.hasPermission(userTeams)) {
                int size = board.get().getBLists().size();
                BList bList = new BList();
                bList.setName(createBoardRequest.getListName());
                bList.setBoard(board.get());
                bList.setPosition(size + 1);
                listService.save(bList);
                return new ResponseEntity<>("List created", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }

        } else {
            return new ResponseEntity<>("Board does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
