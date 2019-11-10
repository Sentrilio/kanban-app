package com.domko.kanbanbackendapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalIdCache;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "user_account")
//@NaturalIdCache
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User implements Serializable {
	@Override
	public String toString() {
		return "User{" +
				"userId=" + userId +
				", email='" + email + '\'' +
				", passwordHash='" + passwordHash + '\'' +
				", nickname='" + nickname + '\'' +
				", teams=" + teams +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return Objects.equals(getUserId(), user.getUserId()) &&
				Objects.equals(getEmail(), user.getEmail()) &&
				Objects.equals(getPasswordHash(), user.getPasswordHash()) &&
				Objects.equals(getNickname(), user.getNickname()) &&
				Objects.equals(getTeams(), user.getTeams());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUserId(), getEmail(), getPasswordHash(), getNickname(), getTeams());
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public List<UserTeam> getTeams() {
		return teams;
	}

	public void setTeams(List<UserTeam> teams) {
		this.teams = teams;
	}

	public User() {
	}

	public User(Long userId, String email, String passwordHash, String nickname, List<UserTeam> teams) {
		this.userId = userId;
		this.email = email;
		this.passwordHash = passwordHash;
		this.nickname = nickname;
		this.teams = teams;
	}

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

	@OneToMany(mappedBy = "user",
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	private List<UserTeam> teams = new ArrayList<>();

//	@JsonIgnore
//	@ManyToMany(mappedBy = "users")
//	private Set<Team> teams = new HashSet<>();

//	public boolean addTeam(Team team){
//		return teams.add(team);
//	}
}
