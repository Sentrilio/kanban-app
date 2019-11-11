package com.domko.kanbanbackendapp.repository;

import com.domko.kanbanbackendapp.model.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

	@Query(value = "SELECT * FROM user_account u WHERE u.email= :email",nativeQuery = true)
	Optional<User> findByEmail(String email);

	Optional<User> findById(Long id);

	@Override
	List<User> findAll();

//	@Override
//	<S extends User> List<S> findAll(Example<S> example);
}
