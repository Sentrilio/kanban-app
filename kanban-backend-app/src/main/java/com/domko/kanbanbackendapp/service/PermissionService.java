package com.domko.kanbanbackendapp.service;

import com.domko.kanbanbackendapp.model.BColumn;
import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.model.Task;
import com.domko.kanbanbackendapp.model.Team;

public interface PermissionService {
    boolean hasPermissionTo(Task task);

    boolean hasPermissionTo(BColumn BColumn);

    boolean hasPermissionTo(Board board);

    boolean hasPermissionTo(Team team);
}
