package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.BColumn;
import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.payload.request.CreateColumnRequest;
import com.domko.kanbanbackendapp.payload.request.UpdateColumnRequest;
import com.domko.kanbanbackendapp.payload.request.UpdateWiPLimit;
import com.domko.kanbanbackendapp.payload.response.MessageResponse;
import com.domko.kanbanbackendapp.repository.BColumnRepository;
import com.domko.kanbanbackendapp.repository.BoardRepository;
import com.domko.kanbanbackendapp.service.BColumnService;
import com.domko.kanbanbackendapp.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BColumnServiceImpl implements BColumnService {

    private final BColumnRepository bColumnRepository;
    private final BoardRepository boardRepository;
    private final PermissionService permissionService;
    private final SimpMessagingTemplate template;

    @Autowired
    public BColumnServiceImpl(BColumnRepository bColumnRepository, BoardRepository boardRepository,
                              PermissionService permissionService, SimpMessagingTemplate template) {
        this.bColumnRepository = bColumnRepository;
        this.boardRepository = boardRepository;
        this.permissionService = permissionService;
        this.template = template;
    }

    @Override
    public boolean updateBColumn(BColumn column, UpdateColumnRequest updateColumnRequest) {
        switch (updateColumnRequest.getOperation()) {
            case MOVE:
                if (updateColumnRequest.getNewIndex() >= 0 && updateColumnRequest.getNewIndex() <= column.getBoard().getColumns().size()) {
                    column.getBoard().getColumns().remove(column);
                    column.getBoard().getColumns().add(updateColumnRequest.getNewIndex(), column);
                    updatePositions(column.getBoard().getColumns());
                    return true;
                } else {
                    return false;
                }
            default:
                return false;
        }
    }

    @Override
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

    @Override
    public BColumn createColumn(Board board, CreateColumnRequest createColumnRequest) {
        System.out.println("requested limit: " + createColumnRequest.getWipLimit());
        BColumn column = new BColumn();
        column.setName(createColumnRequest.getColumnName());
        column.setWipLimit(createColumnRequest.getWipLimit());
        column.setBoard(board);
        column.setPosition(board.getColumns().size());
        column.setTasks(new ArrayList<>());
        return bColumnRepository.save(column);
    }

    @Override
    public ResponseEntity<String> createColumn(CreateColumnRequest createColumnRequest) {
        Optional<Board> board = boardRepository.findById(createColumnRequest.getBoardId());
        if (board.isPresent()) {
            if (permissionService.hasPermissionTo(board.get())) {
                BColumn column = createColumn(board.get(), createColumnRequest);
                if (column != null) {
                    board.get().getColumns().add(column);
                    boardRepository.save(board.get());
                    Optional<Board> updatedBoard = boardRepository.findById(board.get().getId());
                    if (updatedBoard.isPresent()) {
                        String dest = "/topic/board/" + updatedBoard.get().getId();
                        template.convertAndSend(dest, new MessageResponse("board", updatedBoard.get()));
                        return new ResponseEntity<>("Column created", HttpStatus.CREATED);
                    } else {
                        return new ResponseEntity<>("Board does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                } else {
                    return new ResponseEntity<>("Creation unsuccessful", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>("Board does not exists", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> updateBColumn(UpdateColumnRequest updateColumnRequest) {
        Optional<BColumn> bColumn = bColumnRepository.findById(updateColumnRequest.getColumnId());
        if (bColumn.isPresent()) {
            if (permissionService.hasPermissionTo(bColumn.get())) {
                if (updateBColumn(bColumn.get(), updateColumnRequest)) {
                    Optional<Board> board = boardRepository.findById(bColumn.get().getBoard().getId());
                    if (board.isPresent()) {
                        String dest = "/topic/board/" + bColumn.get().getBoard().getId();
                        template.convertAndSend(dest, new MessageResponse("board", board.get()));
                        return new ResponseEntity<>("Operation " + updateColumnRequest.getOperation() +
                                " on column successful", HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("Board does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                } else {
                    System.out.println("update bColumn unsuccessful. New index: " + updateColumnRequest.getNewIndex());
                    return new ResponseEntity<>("Column could not be updated", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>("Unauthorized", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("BColumn does not exists", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> delete(Long columnId) {
        Optional<BColumn> bColumn = bColumnRepository.findById(columnId);
        if (bColumn.isPresent()) {
            if (permissionService.hasPermissionTo(bColumn.get())) {
//                trendRepository.deleteAll(bColumn.get().getTrends());
                bColumn.get().getBoard().getColumns().remove(bColumn.get());
                bColumn.get().getTasks().removeAll(bColumn.get().getTasks());
                boardRepository.save(bColumn.get().getBoard());
                bColumnRepository.delete(bColumn.get());
                updatePositions(bColumn.get().getBoard().getColumns());
                Optional<Board> board = boardRepository.findById(bColumn.get().getBoard().getId());
                if (board.isPresent()) {
                    String dest = "/topic/board/" + bColumn.get().getBoard().getId();
                    template.convertAndSend(dest, new MessageResponse("board", board.get()));
                    return new ResponseEntity<>("Column Deleted", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Board does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>("Unauthorized", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Column does not exists", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> updateLimit(UpdateWiPLimit updateWiPLimit) {
        Optional<BColumn> column = bColumnRepository.findById(updateWiPLimit.getColumnId());
        if (column.isPresent()) {
            if (permissionService.hasPermissionTo(column.get())) {
                if (updateWiPLimit.getLimit() >= 0) {
                    column.get().setWipLimit(updateWiPLimit.getLimit());
                    bColumnRepository.save(column.get());
                    Optional<Board> board = boardRepository.findById(column.get().getBoard().getId());
                    if (board.isPresent()) {
                        String dest = "/topic/board/" + column.get().getBoard().getId();
                        template.convertAndSend(dest, new MessageResponse("board", board.get()));
                        return new ResponseEntity<>("Column's limit changed", HttpStatus.ACCEPTED);
                    } else {
                        return new ResponseEntity<>("Board does not exists", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                } else {
                    return new ResponseEntity<>("Limit is not in range 0-15", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>("Unauthorized", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Column does not exists", HttpStatus.BAD_REQUEST);
        }

    }
}


