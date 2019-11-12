package com.domko.kanbanbackendapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTeamKey implements Serializable {

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "team_id")
	private Long teamId;
}
