package com.domko.kanbanbackendapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "board_statistic")
public class BoardStatistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "arrival_of_tasks", nullable = false)
    private int arrivalOfTasks;

    @Column(name = "number_of_tasks", nullable = false)
    private double numberOfTasks;
}
