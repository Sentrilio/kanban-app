package com.domko.kanbanbackendapp.repository;

import com.domko.kanbanbackendapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

//	@Query("SELECT * FROM user_account u WHERE u.email='email'")
	Optional<User> findByEmail(String email);

	Optional<User> findById(Long id);

}
