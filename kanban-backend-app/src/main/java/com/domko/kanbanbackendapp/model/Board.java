package com.domko.kanbanbackendapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.stereotype.Service;

import javax.persistence.*;
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
	@Column(name = "board_id")
	private Long boardId;

	@Column(name = "name")
	private String name;

	@Column(name = "wip_limit")
	private Integer wipLimit;


	@OneToMany(mappedBy = "board")
	private Set<Task> tasks;

	@JsonManagedReference
	@ManyToOne
	@JoinColumn(name = "team_id", nullable = false)
	private Team team;

	@OneToMany(mappedBy = "board")
	private Set<List> lists;


	public void addTask(Task task) {
		tasks.add(task);
	}
}
