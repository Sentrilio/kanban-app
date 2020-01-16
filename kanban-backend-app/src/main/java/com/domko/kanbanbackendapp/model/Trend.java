package com.domko.kanbanbackendapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "trend")
public class Trend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "column_id", nullable = false)
    private BColumn column;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "elements", nullable = false)
    private Integer elements;
}
