package com.domko.kanbanbackendapp.payload.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateColumnRequest {
    @NotNull
    private Long columnId;
    @NotNull
    private Integer newIndex;
    @NotNull
    private Integer oldIndex;
    @NotNull
    private Operation operation;

}
