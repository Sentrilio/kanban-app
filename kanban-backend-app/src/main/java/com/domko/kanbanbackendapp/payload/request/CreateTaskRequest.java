package com.domko.kanbanbackendapp.payload.request;

import lombok.Data;

@Data
public class CreateTaskRequest {
    private Long listId;
    private Long boardId;

}
