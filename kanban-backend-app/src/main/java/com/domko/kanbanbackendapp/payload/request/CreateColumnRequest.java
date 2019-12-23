package com.domko.kanbanbackendapp.payload.request;

import lombok.Data;

@Data
public class CreateColumnRequest {
    private String columnName;
    private long boardId;
}
