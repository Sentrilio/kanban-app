package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.payload.request.CreateColumnRequest;
import com.domko.kanbanbackendapp.payload.request.UpdateColumnRequest;
import com.domko.kanbanbackendapp.service.implementation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/column")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BColumnController {

    private final BColumnServiceImpl bColumnService;

    @Autowired
    public BColumnController(BColumnServiceImpl bColumnService) {
        this.bColumnService = bColumnService;
    }

    @PostMapping(value = "/create")
    public ResponseEntity<String> createColumn(@RequestBody CreateColumnRequest createColumnRequest) {
        return bColumnService.createColumn(createColumnRequest);
    }

    @PostMapping(value = "/change-position")
    public ResponseEntity<String> handleBColumnMove(@RequestBody UpdateColumnRequest updateColumnRequest) {
        return bColumnService.updateBColumn(updateColumnRequest);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<String> handleDeleteBColumn(@RequestBody Long column) {
        return bColumnService.delete(column);
    }

}
