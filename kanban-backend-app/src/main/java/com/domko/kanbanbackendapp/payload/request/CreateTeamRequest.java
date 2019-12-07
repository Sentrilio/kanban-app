package com.domko.kanbanbackendapp.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CreateTeamRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String teamName;

}
