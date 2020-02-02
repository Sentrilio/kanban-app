package com.domko.kanbanbackendapp.payload.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateWiPLimit {
    @NotNull
    private long columnId;
    @NotNull
    private int limit;
}
