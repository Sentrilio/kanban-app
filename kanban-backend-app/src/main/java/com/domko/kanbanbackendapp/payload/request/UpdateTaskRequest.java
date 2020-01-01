package com.domko.kanbanbackendapp.payload.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateTaskRequest {
    @NotNull
    private Long columnId;
    @NotNull
    private Long taskId;
    @NotNull
    private Integer newIndex;
    @NotNull
    private Operation operation;
    @NotNull
    private Integer oldIndex;

}
