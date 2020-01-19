package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.SeriesSet;
import com.domko.kanbanbackendapp.payload.request.TrendRequest;
import com.domko.kanbanbackendapp.service.implementation.TrendServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/trend")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TrendController {

    private final TrendServiceImpl trendService;

    @Autowired
    public TrendController(TrendServiceImpl trendService) {
        this.trendService = trendService;
    }

    @PostMapping(value = "/get")
    public ResponseEntity<SeriesSet> getTrends(@RequestBody TrendRequest trendRequest) {
        return trendService.getTrendsFromLastDays(trendRequest.getBoardId());
    }
}
