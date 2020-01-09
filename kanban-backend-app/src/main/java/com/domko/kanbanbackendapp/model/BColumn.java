package com.domko.kanbanbackendapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

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
    @OneToMany(mappedBy = "column",fetch = FetchType.LAZY)
    private List<Task> tasks;
}
