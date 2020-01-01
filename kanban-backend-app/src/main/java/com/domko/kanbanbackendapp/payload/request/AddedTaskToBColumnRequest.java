package com.domko.kanbanbackendapp.payload.request;

import lombok.Data;

@Data
public class AddedTaskToBColumnRequest {
    private Long bColumnId;
    private Long taskId;
    private Integer newPosition;
}
