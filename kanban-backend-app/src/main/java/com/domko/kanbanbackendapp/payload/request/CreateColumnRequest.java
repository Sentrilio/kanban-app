package com.domko.kanbanbackendapp.payload.request;

import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class CreateColumnRequest {
    @NotNull
    private long boardId;
    @NotNull
    private String columnName;
    @NotNull
    private Integer wipLimit;
}
