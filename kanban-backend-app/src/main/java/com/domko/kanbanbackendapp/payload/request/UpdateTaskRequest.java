package com.domko.kanbanbackendapp.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
