package com.domko.kanbanbackendapp.service;

import com.domko.kanbanbackendapp.model.BColumn;
import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.payload.request.CreateColumnRequest;
import com.domko.kanbanbackendapp.payload.request.UpdateColumnRequest;
import com.domko.kanbanbackendapp.payload.request.UpdateWiPLimit;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface BColumnService {

    boolean updateBColumn(BColumn bColumn, UpdateColumnRequest updateColumnRequest);

    void updatePositions(List<BColumn> columns);

    BColumn createColumn(Board board, CreateColumnRequest createColumnRequest);

    ResponseEntity<String> createColumn(CreateColumnRequest createColumnRequest);

    ResponseEntity<String> updateBColumn(UpdateColumnRequest updateColumnRequest);

    ResponseEntity<String> delete(Long columnId);

    ResponseEntity<String> updateLimit(UpdateWiPLimit updateWiPLimit);
}
