package com.domko.kanbanbackendapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "team")
public class Team {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;

	@JsonIgnore
	@OneToMany(mappedBy = "team")
	private List<UserTeam> userTeams;

	@JsonManagedReference
	@OneToMany(mappedBy = "team")
	private List<Board> boards;

	public Team(String name) {
		this.name = name;
	}

	public void addBoard(Board board) {
		boards.add(board);
	}
}
