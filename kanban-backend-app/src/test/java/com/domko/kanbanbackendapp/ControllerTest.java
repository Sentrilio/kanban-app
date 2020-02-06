package com.domko.kanbanbackendapp;

import com.domko.kanbanbackendapp.config.WebSecurityConfig;
import com.domko.kanbanbackendapp.controller.BoardController;
import com.domko.kanbanbackendapp.controller.TaskController;
import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.service.implementation.BoardServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BoardController.class)
public class ControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    @InjectMocks
    private BoardServiceImpl service;

    @Autowired
    private WebSecurityConfig webSecurityConfig;

    @Test
//    @WithMockUser(username="spring")
    public void givenEmployees_whenGetEmployees_thenReturnJsonArray() throws Exception {

        Board board = new Board();

        given(service.getBoardById(1)).willReturn(new ResponseEntity<>(board, HttpStatus.OK));

//        mvc.perform(get("/api/board/get/1")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].name", is(alex.getName())));
    }
}
