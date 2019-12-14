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

//	@Column(name = "board_id", updatable = false, insertable = false)
//	private Long boardId;

	@Column(name = "description")
	private String description;

	@Column(name = "content")
	private String content;

//	@JsonIgnore
	@JsonManagedReference
	@ManyToOne
	@JoinColumn(name = "board_id", nullable = false)
	private Board board;

	@JsonManagedReference
	@ManyToOne
	@JoinColumn(name = "list_id", nullable = false)
	private List list;

	@JsonIgnore
	@OneToMany(mappedBy = "task")
	private Set<UserTask> userTasks;

}

