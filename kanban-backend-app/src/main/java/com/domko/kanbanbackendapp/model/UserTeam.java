package com.domko.kanbanbackendapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_team")
public class UserTeam {

	@EmbeddedId
	private UserTeamKey id;

	@ManyToOne
	@MapsId("user_id")
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@MapsId("team_id")
	@JoinColumn(name = "team_id")
	private Team team;

	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	private TeamRole role;

}
