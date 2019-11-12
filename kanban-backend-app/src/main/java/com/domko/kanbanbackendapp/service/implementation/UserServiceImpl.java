package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.User;
import com.domko.kanbanbackendapp.repository.UserRepository;
import com.domko.kanbanbackendapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}


	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public List<User> findUsers(List<Long> idList) {
		return userRepository.findAll();
	}

	public Optional<User> findUser(Long id) {
		return userRepository.findById(id);
	}

	public List<User> findAll(){
		return userRepository.findAll();
	}


}
