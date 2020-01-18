package com.domko.kanbanbackendapp.payload.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TrendRequest {
    @NotNull
    private long boardId;
    @NotNull
    private int days;
}
