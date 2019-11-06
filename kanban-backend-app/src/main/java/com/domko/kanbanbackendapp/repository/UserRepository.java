package com.domko.kanbanbackendapp.repository;

import com.domko.kanbanbackendapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

	Optional<User> findByEmail(String email);
}
