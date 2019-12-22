package com.domko.kanbanbackendapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @Column(name = "task_id")
    private Long taskId;

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
    @JoinColumn(name = "list_id", nullable = false)
    private BList bList;

    @JsonIgnore
    @OneToMany(mappedBy = "task")
    private Set<UserTask> userTasks;


}

