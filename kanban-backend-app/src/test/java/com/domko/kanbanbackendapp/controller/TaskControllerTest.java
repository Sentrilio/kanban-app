package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.SeriesSet;
import com.domko.kanbanbackendapp.service.implementation.TaskServiceImpl;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskServiceImpl taskService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);//required
    }

    @Test
    @WithMockUser
    public void givenMockUser_whenDeleteTask_then200() throws Exception {
        given(taskService.deleteTask(any(Long.class))).willReturn(new ResponseEntity<>("Task Deleted", HttpStatus.OK));
        mockMvc.perform(delete("/api/task/delete/100")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void givenMockUser_whenDeleteTask_then403() throws Exception {
        given(taskService.deleteTask(any(Long.class)))
                .willReturn(new ResponseEntity<>("Unauthorized", HttpStatus.FORBIDDEN));
        mockMvc.perform(delete("/api/task/delete/300")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
