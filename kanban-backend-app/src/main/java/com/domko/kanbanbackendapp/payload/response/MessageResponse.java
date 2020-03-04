package com.domko.kanbanbackendapp.payload.response;

import com.domko.kanbanbackendapp.model.Board;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;

@Data
@AllArgsConstructor
public class MessageResponse {
    private String message;
    private Board board;

    public MessageResponse(String message) {
        this.message = message;
    }
}