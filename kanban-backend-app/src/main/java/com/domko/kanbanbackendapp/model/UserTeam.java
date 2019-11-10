package com.domko.kanbanbackendapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "user_team")
public class UserTeam implements Serializable {

	@EmbeddedId
	private UserTeamId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("userId")
//	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("teamId")
//	@JoinColumn(name = "team_id")
	private Team team;

	@Column(name = "team_role")
	@Enumerated(EnumType.STRING)
	private TeamRole teamRole = TeamRole.MEMBER;

	@Override
	public String toString() {
		return "UserTeam{" +
				"id=" + id +
				", user=" + user +
				", team=" + team +
				", teamRole=" + teamRole +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserTeam userTeam = (UserTeam) o;
		return Objects.equals(getId(), userTeam.getId()) &&
				Objects.equals(getUser(), userTeam.getUser()) &&
				Objects.equals(getTeam(), userTeam.getTeam()) &&
				getTeamRole() == userTeam.getTeamRole();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getUser(), getTeam(), getTeamRole());
	}

	public UserTeamId getId() {
		return id;
	}

	public void setId(UserTeamId id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public TeamRole getTeamRole() {
		return teamRole;
	}

	public void setTeamRole(TeamRole teamRole) {
		this.teamRole = teamRole;
	}

	public UserTeam() {
	}

	public UserTeam(UserTeamId id, User user, Team team, TeamRole teamRole) {
		this.id = id;
		this.user = user;
		this.team = team;
		this.teamRole = teamRole;
	}
}

