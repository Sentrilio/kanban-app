package com.domko.kanbanbackendapp.payload.request;

import lombok.Data;

@Data
public class CreateListRequest {
    private String listName;
    private long boardId;
}
