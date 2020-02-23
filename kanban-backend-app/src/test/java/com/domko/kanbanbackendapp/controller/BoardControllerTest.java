package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.service.implementation.BoardServiceImpl;
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
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoardServiceImpl boardService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);//required
    }

    @Test
    @WithMockUser
    public void givenMockUser_whenGetBoards_then200() throws Exception {
        given(boardService.getUserBoards()).willReturn(new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK));
        mockMvc.perform(get("/api/board/get")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void givenUnauthorizedUser_whenGetBoards_then401() throws Exception {
        given(boardService.getUserBoards()).willReturn(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
        mockMvc.perform(get("/api/board/get")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void givenMockUser_whenGetBoardById_then200() throws Exception {
        long boardId = 1;
        given(boardService.getBoardById(any(Long.class))).willReturn(new ResponseEntity<>(new Board(), HttpStatus.OK));
        mockMvc.perform(get("/api/board/get/" + boardId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenUnauthorizedUser_whenGetBoardById_then401() throws Exception {
        given(boardService.getBoardById(1)).willReturn(new ResponseEntity<>(new Board(), HttpStatus.OK));
        mockMvc.perform(get("/api/board/get/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}

