package com.domko.kanbanbackendapp.service;

import com.domko.kanbanbackendapp.model.Task;

import java.util.Optional;

public interface TaskService {

	Optional<Task> findById(Long id);
}
