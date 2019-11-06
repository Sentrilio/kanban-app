package com.domko.kanbanbackendapp.service;

import com.domko.kanbanbackendapp.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

	User save(User user);
	Optional<User> findByEmail(String email);
	List<User> findUsers(List<Long> idList);
	Optional<User> findUser(Long id);
}
