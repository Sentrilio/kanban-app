package com.domko.kanbanbackendapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
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

	@Column(name = "team_role")
	@Enumerated(EnumType.STRING)
	private TeamRole teamRole;
}
