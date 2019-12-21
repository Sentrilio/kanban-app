package com.domko.kanbanbackendapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "list")
public class BList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "list_id")
    private Long listId;

    @Column(name = "name", nullable = false)
    private String name;

//    @JsonManagedReference
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "position", unique = true, nullable = false)
    private Integer position;

    @JsonIgnore
    @OneToMany(mappedBy = "list")
    private Set<Task> tasks;
}
