package com.domko.kanbanbackendapp.service;

import com.domko.kanbanbackendapp.model.BColumn;
import com.domko.kanbanbackendapp.payload.request.UpdateColumnRequest;

import java.util.Optional;

public interface BColumnService {

    boolean updateBColumn(BColumn bColumn, UpdateColumnRequest updateColumnRequest);

}
