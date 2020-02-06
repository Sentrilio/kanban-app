package com.domko.kanbanbackendapp;

import com.domko.kanbanbackendapp.model.BColumn;
import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.model.Task;
import com.domko.kanbanbackendapp.payload.request.Operation;
import com.domko.kanbanbackendapp.payload.request.UpdateTaskRequest;
import com.domko.kanbanbackendapp.repository.BColumnRepository;
import com.domko.kanbanbackendapp.repository.TaskRepository;
import com.domko.kanbanbackendapp.service.TaskService;
import com.domko.kanbanbackendapp.service.TrendService;
import com.domko.kanbanbackendapp.service.implementation.PermissionService;
import com.domko.kanbanbackendapp.service.implementation.TaskServiceImpl;
import com.domko.kanbanbackendapp.service.implementation.TrendServiceImpl;
import net.bytebuddy.asm.Advice;
import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServiceTest {

    @Mock
    TaskRepository taskRepository;
    @Mock
    BColumnRepository bColumnRepository;
    @Mock
    TrendServiceImpl trendService;
    @Mock
    PermissionService permissionService;
    @Mock
    SimpMessagingTemplate template;

    @InjectMocks
    TaskServiceImpl taskService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);//required
    }

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

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
                .handleTaskUpdate(any(Task.class),any(BColumn.class),any(UpdateTaskRequest.class));//has to be in this form cause of spy
//        when(taskServiceSpy.handleTaskUpdate(any(Task.class),any(BColumn.class),any(UpdateTaskRequest.class))).thenReturn(true);
        doNothing().when(trendService).updateBoardTrends(any());

        ResponseEntity response = taskServiceSpy.updateTask(updateTaskRequest);
        System.out.println(response);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
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
        Mockito.doReturn(true).when(taskServiceSpy)
                .handleTaskUpdate(any(Task.class),any(BColumn.class),any(UpdateTaskRequest.class));//has to be in this form cause of spy
//        when(taskServiceSpy.handleTaskUpdate(any(Task.class),any(BColumn.class),any(UpdateTaskRequest.class))).thenReturn(true);
        doNothing().when(trendService).updateBoardTrends(any());

        ResponseEntity response = taskServiceSpy.updateTask(updateTaskRequest);
        System.out.println(response);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}
