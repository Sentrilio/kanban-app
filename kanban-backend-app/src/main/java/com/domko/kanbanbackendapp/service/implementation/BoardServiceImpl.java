package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.model.Team;
import com.domko.kanbanbackendapp.model.User;
import com.domko.kanbanbackendapp.model.UserTeam;
import com.domko.kanbanbackendapp.payload.request.CreateBoardRequest;
import com.domko.kanbanbackendapp.repository.BoardRepository;
import com.domko.kanbanbackendapp.repository.TeamRepository;
import com.domko.kanbanbackendapp.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final UserServiceImpl userService;
    private final UserTeamServiceImpl userTeamService;
    private final PermissionService permissionService;
    private final TeamRepository teamRepository;

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository, UserServiceImpl userService,
                            UserTeamServiceImpl userTeamService, PermissionService permissionService,
                            TeamRepository teamRepository) {
        this.boardRepository = boardRepository;
        this.userService = userService;
        this.userTeamService = userTeamService;
        this.permissionService = permissionService;
        this.teamRepository = teamRepository;
    }

    @Override
    public List<Board> findAllBoards() {
        return boardRepository.findAll();
    }

    @Override
    public Board save(Board board) {
        return boardRepository.save(board);
    }

    public Optional<Board> findBoard(Long id) {
        return boardRepository.findById(id);
    }

    private void createBoard(CreateBoardRequest createBoardRequest, Team team) {
        Board board = new Board();
        board.setName(createBoardRequest.getBoardName());
        board.setTeam(team);
        save(board);
    }

    public ResponseEntity<String> createBoard(CreateBoardRequest createBoardRequest) {
        Optional<Team> team = teamRepository.findById(createBoardRequest.getTeamId());
        if (team.isPresent()) {
            if (permissionService.hasPermissionTo(team.get())) {
                createBoard(createBoardRequest, team.get());
                return new ResponseEntity<>("Board created", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("You do not participate in this team", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Team does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Board>> getUserBoards() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userService.findByUsername(authentication.getName());
        if (user.isPresent()) {
            System.out.println("user present");
            List<UserTeam> userTeams = userTeamService.findTeamsOfUser(user.get().getId());
            List<Board> boards = new ArrayList<>();
            userTeams.forEach(e -> boards.addAll(e.getTeam().getBoards()));
            return new ResponseEntity<>(boards, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

    }

    public ResponseEntity<Board> getBoardById(long boardId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userService.findByUsername(authentication.getName());
        if (user.isPresent()) {
            Optional<Board> board = boardRepository.findById(boardId);
            return board.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
