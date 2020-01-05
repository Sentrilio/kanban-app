package com.domko.kanbanbackendapp.service;

import com.domko.kanbanbackendapp.model.BColumn;

import java.util.Optional;

public interface BColumnService {
    BColumn save(BColumn BColumn);

    Optional<BColumn> findBColumn(Long id);
}
