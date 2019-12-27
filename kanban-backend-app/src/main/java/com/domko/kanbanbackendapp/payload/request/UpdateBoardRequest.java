package com.domko.kanbanbackendapp.payload.request;

import com.domko.kanbanbackendapp.model.BColumn;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UpdateBoardRequest {

    @NotNull
    private Long boardId;
    @NotNull
    private List<BColumn> columns;
}
