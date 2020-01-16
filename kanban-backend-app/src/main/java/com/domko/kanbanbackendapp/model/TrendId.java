package com.domko.kanbanbackendapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrendId implements Serializable {

    @Column(name = "column_id")
    private Long column;
    @Column(name = "date")
    private Date date;
    @Column(name = "importance")
    private Integer importance;
}
