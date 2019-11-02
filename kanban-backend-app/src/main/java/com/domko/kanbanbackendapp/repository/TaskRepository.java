package com.domko.kanbanbackendapp.repository;

import com.domko.kanbanbackendapp.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
