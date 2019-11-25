package com.domko.kanbanbackendapp.model;

import lombok.*;
import net.bytebuddy.asm.Advice;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_task")
public class UserTask {

	@EmbeddedId
	private UserTaskKey id;

	@ManyToOne
	@MapsId("user_id")
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@MapsId("task_id")
	@JoinColumn(name = "task_id")
	private Task task;

}
