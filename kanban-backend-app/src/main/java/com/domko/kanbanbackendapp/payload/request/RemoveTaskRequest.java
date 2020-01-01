package com.domko.kanbanbackendapp.payload.request;

import com.domko.kanbanbackendapp.model.Task;
import lombok.Data;

import java.util.List;

@Data
public class RemoveTaskRequest {
    private Long bColumnId;
    private Integer taskIndex;
    private Long taskId;
//    private List<Task> tasks;
}
