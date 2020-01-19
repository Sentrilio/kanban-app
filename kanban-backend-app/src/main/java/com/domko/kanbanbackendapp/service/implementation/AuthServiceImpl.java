package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.ERole;
import com.domko.kanbanbackendapp.model.Role;
import com.domko.kanbanbackendapp.model.User;
import com.domko.kanbanbackendapp.payload.request.LoginRequest;
import com.domko.kanbanbackendapp.payload.request.SignupRequest;
import com.domko.kanbanbackendapp.payload.response.JwtResponse;
import com.domko.kanbanbackendapp.payload.response.MessageResponse;
import com.domko.kanbanbackendapp.repository.RoleRepository;
import com.domko.kanbanbackendapp.repository.UserRepository;
import com.domko.kanbanbackendapp.security.UserDetailsImpl;
import com.domko.kanbanbackendapp.security.jwt.JwtUtils;
import com.domko.kanbanbackendapp.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository,
                           RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        //it has to be changed because anybody can make request and grant all roles to his account
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Optional<Role> adminRole = roleRepository.findByName(ERole.ROLE_ADMIN);
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        adminRole.ifPresent(roles::add);

                        break;
                    case "mod":
                        Optional<Role> modRole = roleRepository.findByName(ERole.ROLE_MODERATOR);
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        modRole.ifPresent(roles::add);

                        break;
                    default:
                        Optional<Role> userRole = roleRepository.findByName(ERole.ROLE_USER);
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        userRole.ifPresent(roles::add);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
