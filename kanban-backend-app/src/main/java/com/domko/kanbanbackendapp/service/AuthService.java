package com.domko.kanbanbackendapp.service;

import com.domko.kanbanbackendapp.payload.request.LoginRequest;
import com.domko.kanbanbackendapp.payload.request.SignupRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> authenticateUser(LoginRequest loginRequest);

    ResponseEntity<?> registerUser(SignupRequest signUpRequest);
}
