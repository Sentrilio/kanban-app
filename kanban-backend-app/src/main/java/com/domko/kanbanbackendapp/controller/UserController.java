package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.User;
import com.domko.kanbanbackendapp.service.implementation.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserServiceImpl userService;

	@PostMapping(value = "/create", consumes = "application/json;charset=UTF-8")
	public User createUser(@RequestBody User user) {
		return userService.save(user);
	}

	@GetMapping(value = "/all")
	public List<User> getUsers(){
		return userService.findAll();
	}

	@GetMapping(value = "/get/{email}")
	public Optional<User> findByEmail(@PathVariable String email) {
		return userService.findByEmail(email);
	}
}
