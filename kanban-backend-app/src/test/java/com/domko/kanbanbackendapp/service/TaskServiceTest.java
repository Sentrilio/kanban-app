package com.domko.kanbanbackendapp.service;

import com.domko.kanbanbackendapp.model.BColumn;
import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.model.Task;
import com.domko.kanbanbackendapp.payload.request.Operation;
import com.domko.kanbanbackendapp.payload.request.UpdateTaskRequest;
import com.domko.kanbanbackendapp.repository.BColumnRepository;
import com.domko.kanbanbackendapp.repository.TaskRepository;
import com.domko.kanbanbackendapp.service.implementation.PermissionServiceImpl;
import com.domko.kanbanbackendapp.service.implementation.TaskServiceImpl;
import com.domko.kanbanbackendapp.service.implementation.TrendServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceTest {
    @Mock
    TaskRepository taskRepository;
    @Mock
    BColumnRepository bColumnRepository;
    @Mock
    TrendServiceImpl trendService;
    @Mock
    PermissionServiceImpl permissionService;
    //even tough there is no method call on template it has to be declared in order to inject all mocks into TaskServiceImpl
    @Mock
    SimpMessagingTemplate template;
    @InjectMocks
    TaskServiceImpl taskService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);//required
    }

//    @Rule
//    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Test
    public void successfulTaskUpdate() {
        TaskServiceImpl taskServiceSpy = Mockito.spy(this.taskService);
        UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest(1l, 1l, 1, Operation.MOVE, 3);
        Board board = new Board();
        BColumn bColumn = new BColumn();
        bColumn.setBoard(board);
        Optional<BColumn> bColumnOptional = Optional.of(bColumn);
        Task task = new Task();
        task.setColumn(bColumn);
        Optional<Task> taskOptional = Optional.of(task);

        when(taskRepository.findById(any(Long.class))).thenReturn(taskOptional);
        when(bColumnRepository.findById(any(Long.class))).thenReturn(bColumnOptional);
        when(permissionService.hasPermissionTo(any(Task.class))).thenReturn(true);
        Mockito.doReturn(true).when(taskServiceSpy)
                .handleTaskUpdate(any(Task.class), any(BColumn.class), any(UpdateTaskRequest.class));//has to be in this form cause of spy
        doNothing().when(trendService).updateBoardTrends(any());

        ResponseEntity response = taskServiceSpy.updateTask(updateTaskRequest);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void unsuccessfulTaskUpdate() {
        TaskServiceImpl taskServiceSpy = Mockito.spy(this.taskService);
        Board board = new Board();
        BColumn bColumn = new BColumn();
        bColumn.setBoard(board);
        Optional<BColumn> bColumnOptional = Optional.of(bColumn);
        Task task = new Task();
        task.setColumn(bColumn);
        Optional<Task> taskOptional = Optional.of(task);

        when(taskRepository.findById(any(Long.class))).thenReturn(taskOptional);
        when(bColumnRepository.findById(any(Long.class))).thenReturn(bColumnOptional);
        when(permissionService.hasPermissionTo(any(Task.class))).thenReturn(true);
        Mockito.doReturn(false).when(taskServiceSpy).handleTaskUpdate(
                any(Task.class), any(BColumn.class), any(UpdateTaskRequest.class));//has to be in this form cause of spy
        doNothing().when(trendService).updateBoardTrends(any());

        ResponseEntity response = taskServiceSpy.updateTask(
                new UpdateTaskRequest(1l, 1l, 1, Operation.MOVE, 3));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void userWithNoPermissionTaskUpdateRequest() {
        TaskServiceImpl taskServiceSpy = Mockito.spy(this.taskService);
        UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest(1l, 1l, 1, Operation.MOVE, 3);
        Board board = new Board();
        BColumn bColumn = new BColumn();
        bColumn.setBoard(board);
        Optional<BColumn> bColumnOptional = Optional.of(bColumn);

        Task task = new Task();
        task.setColumn(bColumn);
        Optional<Task> taskOptional = Optional.of(task);
        when(taskRepository.findById(any(Long.class))).thenReturn(taskOptional);
        when(bColumnRepository.findById(any(Long.class))).thenReturn(bColumnOptional);
        when(permissionService.hasPermissionTo(any(Task.class))).thenReturn(false);
        Mockito.doReturn(true).when(taskServiceSpy).handleTaskUpdate(
                any(Task.class), any(BColumn.class), any(UpdateTaskRequest.class));//has to be in this form cause of spy
        doNothing().when(trendService).updateBoardTrends(any());

        ResponseEntity response = taskServiceSpy.updateTask(updateTaskRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void updateTaskUpdateRequestWithNonExistingTaskAndBoardId() {
        TaskServiceImpl taskServiceSpy = Mockito.spy(this.taskService);
        UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest(1l, 1l, 1, Operation.MOVE, 3);
        when(taskRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        when(bColumnRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        ResponseEntity response = taskServiceSpy.updateTask(updateTaskRequest);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
