package com.domko.kanbanbackendapp.payload.request;

import lombok.Data;

@Data
public class MoveTaskRequest {
    private Long bColumnId;
    private Long taskId;
    private Integer oldIndex;
    private Integer newIndex;
}
