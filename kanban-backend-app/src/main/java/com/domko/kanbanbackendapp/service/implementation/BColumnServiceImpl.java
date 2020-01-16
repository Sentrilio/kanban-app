package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.BColumn;
import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.payload.request.CreateColumnRequest;
import com.domko.kanbanbackendapp.payload.request.UpdateColumnRequest;
import com.domko.kanbanbackendapp.repository.BColumnRepository;
import com.domko.kanbanbackendapp.service.BColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@org.springframework.transaction.annotation.Transactional
public class BColumnServiceImpl implements BColumnService {

    @Autowired
    private BColumnRepository bColumnRepository;

    @Override
    public BColumn save(BColumn BColumn) {
        return bColumnRepository.save(BColumn);
    }

    @Override
    public Optional<BColumn> findById(Long id) {
        return bColumnRepository.findById(id);
    }

    @Transactional
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

    @Override
    public void delete(BColumn bColumn) {
        bColumnRepository.delete(bColumn);
    }

    @Transactional//maybe not needed (to test)
    public void updatePositions(List<BColumn> columns) {
        for (int i = 0; i < columns.size(); i++) {
            Optional<BColumn> column = findById(columns.get(i).getId());
            if (column.isPresent()) {
                column.get().setPosition(i);
                save(column.get());
            } else {
                System.out.println("column does not exists");
            }
        }
    }

    public BColumn createColumn(Board board, CreateColumnRequest createColumnRequest) {
        System.out.println("requested limit: "+ createColumnRequest.getWipLimit());
        BColumn column = new BColumn();
        column.setName(createColumnRequest.getColumnName());
        column.setWipLimit(createColumnRequest.getWipLimit());
        column.setBoard(board);
        column.setPosition(board.getColumns().size());
        System.out.println("column name: " + column.getName() + "wip limit: " + column.getWipLimit());
        return save(column);
    }
}


