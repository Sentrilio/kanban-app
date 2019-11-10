package com.domko.kanbanbackendapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;


@Entity
@Table(name = "team")
public class Team implements Serializable {

	@OneToMany(mappedBy = "team",
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	private List<UserTeam> users = new ArrayList<>();

//	@JsonIgnore
//	@ManyToMany(cascade = {CascadeType.ALL})
//	@JoinTable(
//			name = "user_team",
//			joinColumns = {@JoinColumn(name = "team_id")},
//			inverseJoinColumns = {@JoinColumn(name = "user_id")}
//	)
//	Set<User> users = new HashSet<>();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "team_id")
	private Long teamId;
	@Column(name = "name")
	private String name;

	@Override
	public String toString() {
		return "Team{" +
				"users=" + users +
				", teamId=" + teamId +
				", name='" + name + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Team team = (Team) o;
		return Objects.equals(getUsers(), team.getUsers()) &&
				Objects.equals(getTeamId(), team.getTeamId()) &&
				Objects.equals(getName(), team.getName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUsers(), getTeamId(), getName());
	}

	public List<UserTeam> getUsers() {
		return users;
	}

	public void setUsers(List<UserTeam> users) {
		this.users = users;
	}

	public Long getTeamId() {
		return teamId;
	}

	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Team() {
	}

	public Team(List<UserTeam> users, Long teamId, String name) {
		this.users = users;
		this.teamId = teamId;
		this.name = name;
	}

	public void addUser(User user) {
		UserTeam userTeam = new UserTeam();
		userTeam.setTeam(this);
		userTeam.setUser(user);

		users.add(userTeam);
		user.getTeams().add(userTeam);
	}

	public void removeUser(User user) {
		for (Iterator<UserTeam> iterator = users.iterator(); iterator.hasNext(); ) {
			UserTeam userTeam = iterator.next();

			if (userTeam.getTeam().equals(this) && userTeam.getUser().equals(user)) {
				iterator.remove();
				userTeam.getUser().getTeams().remove(userTeam);
				userTeam.setTeam(null);
				userTeam.setUser(null);
			}
		}
	}

}
