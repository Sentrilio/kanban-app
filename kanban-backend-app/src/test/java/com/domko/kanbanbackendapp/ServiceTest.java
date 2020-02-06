package com.domko.kanbanbackendapp;

import com.domko.kanbanbackendapp.model.BColumn;
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
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
        System.out.println("init method called");
    }

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void testUpdateTask() {
        TaskServiceImpl taskServiceSpy = Mockito.spy(this.taskService);
//        TaskServiceImpl taskServiceMock = Mockito.mock(TaskServiceImpl.class);
        UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest(1l, 1l, 1, Operation.MOVE, 3);
        Task task = new Task();
        Optional<Task> taskOptional = Optional.of(task);
        BColumn bColumn = new BColumn();
        Optional<BColumn> bColumnOptional = Optional.of(bColumn);
        when(taskRepository.findById(any(Long.class))).thenReturn(taskOptional);
        when(bColumnRepository.findById(any(Long.class))).thenReturn(bColumnOptional);
        when(permissionService.hasPermissionTo(any(Task.class))).thenReturn(true);
        Mockito.doReturn(true).when(taskServiceSpy).handleTaskUpdate(any(Task.class),any(BColumn.class),any(UpdateTaskRequest.class));
//        when(taskServiceSpy.handleTaskUpdate(any(Task.class),any(BColumn.class),any(UpdateTaskRequest.class))).thenReturn(true);

//        ResponseEntity response = taskService.updateTask(updateTaskRequest);
//        System.out.println(response);
    }
}
