package com.domko.kanbanbackendapp.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "UserTeam")
@Table(name = "user_team")
@Data
public class UserTeam implements Serializable {

	@Id
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Id
	@ManyToOne
	@JoinColumn(name = "team_id")
	private Team team;

	@Column(name = "team_role")
	private TeamRole teamRole = TeamRole.MEMBER;
}
