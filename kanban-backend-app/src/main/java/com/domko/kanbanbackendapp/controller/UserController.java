package com.domko.kanbanbackendapp.controller;

import com.domko.kanbanbackendapp.model.User;
import com.domko.kanbanbackendapp.service.UserService;
import com.domko.kanbanbackendapp.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserServiceImpl userService;

	@PostMapping(value = "/create")
	public User createUser(@RequestBody User user) {
		return userService.save(user);
	}

}
