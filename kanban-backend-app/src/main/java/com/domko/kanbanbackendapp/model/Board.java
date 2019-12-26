package com.domko.kanbanbackendapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "wip_limit")
    private Integer wipLimit;

//    @OneToMany(mappedBy = "board")
//    private Set<Task> tasks;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;


    @OneToMany(mappedBy = "board")
    private List<BColumn> columns;


//    public void addTask(Task task) {
//        tasks.add(task);
//    }
}
