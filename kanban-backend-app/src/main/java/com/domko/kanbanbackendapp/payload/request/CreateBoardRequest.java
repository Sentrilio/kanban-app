package com.domko.kanbanbackendapp.payload.request;

import lombok.Data;

@Data
public class CreateBoardRequest {
    private String boardName;
    private long teamId;
}
