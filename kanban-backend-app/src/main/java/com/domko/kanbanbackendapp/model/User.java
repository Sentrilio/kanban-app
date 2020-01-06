package com.domko.kanbanbackendapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;


@Entity
@Table(name = "user_account",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = "username"),
				@UniqueConstraint(columnNames = "email")
		})
@Data
public class User implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotBlank
	@Size(max = 20)
	@Column(name = "username")
	private String username;

	@NotBlank
	@Size(max = 50)
	@Column(name = "email")
	private String email;

	@NotBlank
	@Size(max = 120)
	@JsonIgnore
	@Column(name = "password")
	private String password;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<UserTeam> userTeams;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<UserTask> userTasks;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public User() {
	}
}
