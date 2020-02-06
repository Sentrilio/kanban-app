package com.domko.kanbanbackendapp.repository;

import com.domko.kanbanbackendapp.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

	@Override
	Optional<Task> findById(Long id);

}
