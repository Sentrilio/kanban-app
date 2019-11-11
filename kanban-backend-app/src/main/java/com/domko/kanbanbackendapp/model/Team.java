package com.domko.kanbanbackendapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Table(name = "team")
public class Team {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "team_id")
	private Long teamId;

	@Column(name = "name")
	private String name;

	//	@JsonManagedReference
	@JsonIgnore
	@OneToMany(mappedBy = "team")
	private Set<UserTeam> userTeams;

	public Team(String name) {
		this.name = name;
	}
}
