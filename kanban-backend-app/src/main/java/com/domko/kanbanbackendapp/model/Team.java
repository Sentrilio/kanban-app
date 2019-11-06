package com.domko.kanbanbackendapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.internal.build.AllowPrintStacktrace;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "team")
public class Team {
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
}
