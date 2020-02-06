package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.*;
import com.domko.kanbanbackendapp.payload.request.CreateBoardRequest;
import com.domko.kanbanbackendapp.repository.*;
import com.domko.kanbanbackendapp.service.BoardService;
import com.domko.kanbanbackendapp.service.PermissionService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final UserTeamRepository userTeamRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final BoardStatisticRepository boardStatisticRepository;
    private final PermissionService permissionService;

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository, UserTeamRepository userTeamRepository,
                            TeamRepository teamRepository, UserRepository userRepository,
                            BoardStatisticRepository boardStatisticRepository, PermissionService permissionService) {
        this.boardRepository = boardRepository;
        this.userTeamRepository = userTeamRepository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.boardStatisticRepository = boardStatisticRepository;
        this.permissionService = permissionService;
    }

    @Override
    public ResponseEntity<?> createBoard(CreateBoardRequest createBoardRequest) {
        Optional<Team> team = teamRepository.findById(createBoardRequest.getTeamId());
        if (team.isPresent()) {
            if (permissionService.hasPermissionTo(team.get())) {
                Board board = createBoard(createBoardRequest, team.get());
                createBoardStatisticsForTodayAndTomorrow(board);
                return new ResponseEntity<>(board, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("You do not participate in this team", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Team does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Board createBoard(CreateBoardRequest createBoardRequest, Team team) {
        Board board = new Board();
        board.setCreateDate(new Date());
        board.setName(createBoardRequest.getBoardName());
        board.setTeam(team);
        return boardRepository.save(board);
    }

    @Override
    public void createBoardStatisticsForTodayAndTomorrow(Board board) {
        DateTime dateTime = new DateTime();
        for (int i = 0; i < 2; i++) {
            BoardStatistic boardStatistic = new BoardStatistic();
            boardStatistic.setArrivalOfTasks(0);
            boardStatistic.setNumberOfTasks(0);
            boardStatistic.setBoard(board);
            boardStatistic.setDate(dateTime.plusDays(i).toDate());
            boardStatisticRepository.save(boardStatistic);
        }
    }
    @Override
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
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @Override
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

    @Override
    public int getNumberOfTasks(Board board) {
        return board.getColumns()
                .stream()
                .mapToInt(o -> o.getTasks().size())
                .sum();
    }

}
