package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.User;
import com.domko.kanbanbackendapp.service.implementation.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/user")
public class UserController {

	private final UserServiceImpl userService;

	@Autowired
	public UserController(UserServiceImpl userService) {
		this.userService = userService;
	}

//	@GetMapping(value = "/get/{email}")
//	public Optional<User> findByEmail(@PathVariable String email) {
//		return userService.findByEmail(email);
//	}
}
