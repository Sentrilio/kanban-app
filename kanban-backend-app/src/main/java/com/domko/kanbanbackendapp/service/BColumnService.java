package com.domko.kanbanbackendapp.service;

import com.domko.kanbanbackendapp.model.BColumn;
import com.domko.kanbanbackendapp.payload.request.UpdateColumnRequest;

import java.util.Optional;

public interface BColumnService {
    BColumn save(BColumn BColumn);

    Optional<BColumn> findById(Long id);

    boolean updateBColumn(BColumn bColumn, UpdateColumnRequest updateColumnRequest);

    void delete(BColumn bColumn);
}
