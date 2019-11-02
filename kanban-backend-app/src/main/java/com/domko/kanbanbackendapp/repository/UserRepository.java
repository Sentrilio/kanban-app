package com.domko.kanbanbackendapp.repository;

import com.domko.kanbanbackendapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
