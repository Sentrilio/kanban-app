package com.domko.kanbanbackendapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "content")
    private String content;

    @Column(name = "position")
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Integer position;


    @JsonBackReference
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "column_id", nullable = false)
    private BColumn column;

    @JsonIgnore
    @OneToMany(mappedBy = "task")
    private Set<UserTask> userTasks;


}

