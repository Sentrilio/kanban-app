package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.BColumn;
import com.domko.kanbanbackendapp.model.Task;
import com.domko.kanbanbackendapp.payload.request.UpdateColumnRequest;
import com.domko.kanbanbackendapp.repository.BColumnRepository;
import com.domko.kanbanbackendapp.repository.TaskRepository;
import com.domko.kanbanbackendapp.service.BColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.domko.kanbanbackendapp.payload.request.Operation.MOVE;

@Service
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
}


