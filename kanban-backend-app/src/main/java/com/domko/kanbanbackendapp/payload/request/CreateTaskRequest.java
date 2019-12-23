package com.domko.kanbanbackendapp.payload.request;

import lombok.Data;

@Data
public class CreateTaskRequest {
    private Long columnId;
    private String description;
}
