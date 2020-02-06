package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.repository.UserRepository;
import com.domko.kanbanbackendapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}
