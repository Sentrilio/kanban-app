package com.domko.kanbanbackendapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "user_account")
public class User implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;
	@Column(name = "email")
	private String email;
	@Column(name = "password_hash")
	private String passwordHash;
	@Column(name = "nickname")
	private String nickname;

	@JsonIgnore
	@ManyToMany(mappedBy = "users")
	private Set<Team> teams = new HashSet<>();

	public boolean addTeam(Team team){
		return teams.add(team);
	}
}
