package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.SeriesSet;
import com.domko.kanbanbackendapp.payload.request.LoginRequest;
import com.domko.kanbanbackendapp.service.implementation.AuthServiceImpl;
import com.domko.kanbanbackendapp.service.implementation.BoardServiceImpl;
import com.domko.kanbanbackendapp.service.implementation.TrendServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TrendControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrendServiceImpl trendService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);//required
    }

    @Test
    @WithMockUser
    public void givenMockUser_whenGetTrends_then200() throws Exception {
        given(trendService.getTrendsFromLastDays(any(Long.class))).willReturn(new ResponseEntity<>(new SeriesSet(), HttpStatus.OK));
        mockMvc.perform(get("/api/trend/get/100")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser
    public void givenMockUser_whenGetTrends_then404() throws Exception {
        given(trendService.getTrendsFromLastDays(any(Long.class))).willReturn( new ResponseEntity<>(HttpStatus.NOT_FOUND));
        mockMvc.perform(get("/api/trend/get/100")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
