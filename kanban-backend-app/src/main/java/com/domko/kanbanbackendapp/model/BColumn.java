package com.domko.kanbanbackendapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "b_column")
public class BColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "position", unique = true, nullable = false)
    private Integer position;

    @JsonManagedReference
    @OrderColumn(name = "position")// jesli nie ma rekordu to tworzony jest null
    @OneToMany(mappedBy = "column", orphanRemoval = true)
    private List<Task> tasks;

    @Column(name = "wip_limit", nullable = false)
    private Integer wipLimit;

    @JsonManagedReference
    @OneToMany(mappedBy = "column", orphanRemoval = true)
    private List<Trend> trends;
}
