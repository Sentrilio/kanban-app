package com.domko.kanbanbackendapp.service;

import com.domko.kanbanbackendapp.model.User;
import com.domko.kanbanbackendapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public List<User> findUsers(List<Long> idList) {
		return userRepository.findAll();
	}
}
