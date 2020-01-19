package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.BColumn;
import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.payload.request.CreateColumnRequest;
import com.domko.kanbanbackendapp.payload.request.UpdateColumnRequest;
import com.domko.kanbanbackendapp.payload.response.MessageResponse;
import com.domko.kanbanbackendapp.repository.BColumnRepository;
import com.domko.kanbanbackendapp.repository.BoardRepository;
import com.domko.kanbanbackendapp.repository.TaskRepository;
import com.domko.kanbanbackendapp.repository.TrendRepository;
import com.domko.kanbanbackendapp.service.BColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@org.springframework.transaction.annotation.Transactional
public class BColumnServiceImpl implements BColumnService {

    private final BColumnRepository bColumnRepository;
    private final BoardRepository boardRepository;
    private final TrendRepository trendRepository;
    private final TaskRepository taskRepository;
    private final PermissionService permissionService;
    private final SimpMessagingTemplate template;

    @Autowired
    public BColumnServiceImpl(BColumnRepository bColumnRepository, BoardRepository boardRepository,
                              TrendRepository trendRepository, TaskRepository taskRepository, PermissionService permissionService, SimpMessagingTemplate template) {
        this.bColumnRepository = bColumnRepository;
        this.boardRepository = boardRepository;
        this.trendRepository = trendRepository;
        this.taskRepository = taskRepository;
        this.permissionService = permissionService;
        this.template = template;
    }

    public boolean updateBColumn(BColumn column, UpdateColumnRequest updateColumnRequest) {
        switch (updateColumnRequest.getOperation()) {
            case MOVE:
                column.getBoard().getColumns().remove(column);
                column.getBoard().getColumns().add(updateColumnRequest.getNewIndex(), column);
                updatePositions(column.getBoard().getColumns());
                return true;
            default:
                return false;
        }
    }

    public void updatePositions(List<BColumn> columns) {
        for (int i = 0; i < columns.size(); i++) {
            Optional<BColumn> column = bColumnRepository.findById(columns.get(i).getId());
            if (column.isPresent()) {
                column.get().setPosition(i);
                bColumnRepository.save(column.get());
            } else {
                System.out.println("column does not exists");
            }
        }
    }

    private BColumn createColumn(Board board, CreateColumnRequest createColumnRequest) {
        System.out.println("requested limit: "+ createColumnRequest.getWipLimit());
        BColumn column = new BColumn();
        column.setName(createColumnRequest.getColumnName());
        column.setWipLimit(createColumnRequest.getWipLimit());
        column.setBoard(board);
        column.setPosition(board.getColumns().size());
        System.out.println("column name: " + column.getName() + " wip limit: " + column.getWipLimit());
        return bColumnRepository.save(column);
    }

    public ResponseEntity<String> createColumn(CreateColumnRequest createColumnRequest) {
        Optional<Board> board = boardRepository.findById(createColumnRequest.getBoardId());
        if (board.isPresent()) {
            if (permissionService.hasPermissionTo(board.get())) {
                BColumn column = createColumn(board.get(), createColumnRequest);
                template.convertAndSend("/topic/greetings/" + column.getBoard().getId(), new MessageResponse("board updated"));
                return new ResponseEntity<>("Column created", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>("Board does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> updateBColumn(UpdateColumnRequest updateColumnRequest) {
        Optional<BColumn> bColumn = bColumnRepository.findById(updateColumnRequest.getColumnId());
        if (bColumn.isPresent()) {
            if (permissionService.hasPermissionTo(bColumn.get())) {
                if (updateBColumn(bColumn.get(), updateColumnRequest)) {
                    template.convertAndSend("/topic/board/" + bColumn.get().getBoard().getId(), new MessageResponse("board updated"));
                    return new ResponseEntity<>("Operation " + updateColumnRequest.getOperation() + " on column successful", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Column could not be updated", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>("Unauthorized", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("BColumn does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> delete(Long column) {
        Optional<BColumn> bColumn = bColumnRepository.findById(column);
        if (bColumn.isPresent()) {
            if (permissionService.hasPermissionTo(bColumn.get())) {
//                trendRepository.deleteAll(bColumn.get().getTrends());
                bColumn.get().getBoard().getColumns().remove(bColumn.get());
                bColumn.get().getTasks().removeAll(bColumn.get().getTasks());
                boardRepository.save(bColumn.get().getBoard());
                bColumnRepository.delete(bColumn.get());
                template.convertAndSend("/topic/board/" + bColumn.get().getBoard().getId(), new MessageResponse("board updated"));
                return new ResponseEntity<>("Column Deleted", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Unauthorized", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Column does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


