package com.domko.kanbanbackendapp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

//@Data
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "team")
public class Team {

	@JsonManagedReference
	@ManyToMany(cascade = {CascadeType.ALL})
	@JoinTable(
			name = "user_team",
			joinColumns = {@JoinColumn(name = "team_id")},
			inverseJoinColumns = {@JoinColumn(name = "user_id")}
	)
	Set<User> users = new HashSet<>();


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "team_id")
	private Long teamId;
	@Column(name = "name")
	private String name;

	public boolean addUser(User user){
		return users.add(user);
	}

}
