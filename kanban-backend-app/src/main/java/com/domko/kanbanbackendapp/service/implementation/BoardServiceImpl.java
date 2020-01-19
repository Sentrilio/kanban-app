package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.model.Team;
import com.domko.kanbanbackendapp.model.User;
import com.domko.kanbanbackendapp.model.UserTeam;
import com.domko.kanbanbackendapp.payload.request.CreateBoardRequest;
import com.domko.kanbanbackendapp.repository.BoardRepository;
import com.domko.kanbanbackendapp.repository.TeamRepository;
import com.domko.kanbanbackendapp.repository.UserRepository;
import com.domko.kanbanbackendapp.repository.UserTeamRepository;
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
    private final UserTeamRepository userTeamRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final PermissionService permissionService;

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository, UserTeamRepository userTeamRepository,
                            TeamRepository teamRepository, UserRepository userRepository,
                            PermissionService permissionService) {
        this.boardRepository = boardRepository;
        this.userTeamRepository = userTeamRepository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.permissionService = permissionService;
    }

    private void createBoard(CreateBoardRequest createBoardRequest, Team team) {
        Board board = new Board();
        board.setName(createBoardRequest.getBoardName());
        board.setTeam(team);
        boardRepository.save(board);
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
        Optional<User> user = userRepository.findByUsername(authentication.getName());
        if (user.isPresent()) {
            System.out.println("user present");
            List<UserTeam> userTeams = userTeamRepository.findAllTeamsOfUser(user.get().getId());
            List<Board> boards = new ArrayList<>();
            userTeams.forEach(e -> boards.addAll(e.getTeam().getBoards()));
            return new ResponseEntity<>(boards, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

    }

    public ResponseEntity<Board> getBoardById(long boardId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(authentication.getName());
        if (user.isPresent()) {
            Optional<Board> board = boardRepository.findById(boardId);
            return board.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
