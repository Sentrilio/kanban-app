package com.domko.kanbanbackendapp.service;

import com.domko.kanbanbackendapp.model.*;
import com.domko.kanbanbackendapp.repository.UserTeamRepository;
import com.domko.kanbanbackendapp.service.implementation.PermissionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.test.context.support.WithMockUser;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class PermissionServiceTest {

    @Mock
    UserTeamRepository userTeamRepository;

    @InjectMocks
    PermissionServiceImpl permissionService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);//required
    }

    @Test
    public void hasPermissionToTask() {
        PermissionServiceImpl permissionServiceSpy = Mockito.spy(this.permissionService);
        Task task = new Task();
        task.setColumn(new BColumn());
        Mockito.doReturn(true).when(permissionServiceSpy).hasPermissionTo(any(BColumn.class));
        boolean hasPermission = permissionServiceSpy.hasPermissionTo(task);
        assertThat(hasPermission).isTrue();
    }

    @Test
    public void hasPermissionToBColumn() {
        PermissionServiceImpl permissionServiceSpy = Mockito.spy(this.permissionService);
        BColumn bColumn = new BColumn();
        bColumn.setBoard(new Board());
        Mockito.doReturn(true).when(permissionServiceSpy).hasPermissionTo(any(Board.class));
        boolean hasPermission = permissionServiceSpy.hasPermissionTo(bColumn);
        assertThat(hasPermission).isTrue();
    }

    @Test
    public void hasPermissionToBoard() {
        PermissionServiceImpl permissionServiceSpy = Mockito.spy(this.permissionService);
        Board board = new Board();
        board.setTeam(new Team());
        Mockito.doReturn(true).when(permissionServiceSpy).hasPermissionTo(any(Team.class));
        boolean hasPermission = permissionServiceSpy.hasPermissionTo(board);
        assertThat(hasPermission).isTrue();
    }

//    @Test
//    @WithMockUser
//    public void hasPermissionToTeam() {
//        PermissionServiceImpl permissionServiceSpy = Mockito.spy(this.permissionService);
//        Team team = new Team();
//        team.setId(1l);
//        when(userTeamRepository.findAllById_TeamId(any(Long.class))).thenReturn(Arrays.asList(new UserTeam()));
//        Mockito.doReturn(true).when(permissionServiceSpy).hasPermissionTo(any(Team.class));
//        boolean hasPermission = permissionServiceSpy.hasPermissionTo(team);
//        assertThat(hasPermission).isTrue();
//    }
}
