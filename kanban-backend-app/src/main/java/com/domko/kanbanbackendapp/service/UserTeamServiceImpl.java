package com.domko.kanbanbackendapp.service;

import com.domko.kanbanbackendapp.model.UserTeam;
import com.domko.kanbanbackendapp.repository.UserTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserTeamServiceImpl implements UserTeamService {

	@Autowired
	private UserTeamRepository userTeamRepository;

	public UserTeam save(UserTeam userTeam) {
		return userTeamRepository.save(userTeam);
	}
}
