package com.domko.kanbanbackendapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Data
@Entity
@Table(name = "user_account")
public class User implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "email")
	private String email;

	@Column(name = "password_hash")
	private String passwordHash;

	@Column(name = "nickname")
	private String nickname;

//	@JsonBackReference
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private Set<UserTeam> userTeams;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private Set<UserTask> userTasks;

}
