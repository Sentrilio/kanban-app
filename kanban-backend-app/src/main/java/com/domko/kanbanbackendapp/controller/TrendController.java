package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.SeriesSet;
import com.domko.kanbanbackendapp.service.implementation.TrendServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/trend")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TrendController {

    @Autowired
    private TrendServiceImpl trendService;

    @GetMapping(value = "/get/{boardId}")
    public ResponseEntity<SeriesSet> getTrends(@PathVariable("boardId") long boardId) {
        return trendService.getTrendsFromLastDays(boardId, 7);
    }
}
