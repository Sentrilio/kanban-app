package com.domko.kanbanbackendapp.payload.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AddUserRequest {

    @NotNull
    private long teamId;
    @NotNull
    private String email;
}
