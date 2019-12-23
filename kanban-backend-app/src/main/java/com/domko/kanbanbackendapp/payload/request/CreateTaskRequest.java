package com.domko.kanbanbackendapp.payload.request;

import lombok.Data;

@Data
public class CreateTaskRequest {
    private Long boardListId;
    private String description;
}
