package com.domko.kanbanbackendapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserTeamId implements Serializable {
	@Override
	public String toString() {
		return "UserTeamId{" +
				"userId=" + userId +
				", teamId=" + teamId +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserTeamId that = (UserTeamId) o;
		return Objects.equals(getUserId(), that.getUserId()) &&
				Objects.equals(getTeamId(), that.getTeamId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUserId(), getTeamId());
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getTeamId() {
		return teamId;
	}

	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}

	public UserTeamId() {
	}

	public UserTeamId(Long userId, Long teamId) {
		this.userId = userId;
		this.teamId = teamId;
	}

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "team_id")
	private Long teamId;
}
