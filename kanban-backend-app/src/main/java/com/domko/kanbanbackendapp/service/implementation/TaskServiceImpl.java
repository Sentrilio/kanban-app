package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.Task;
import com.domko.kanbanbackendapp.repository.TaskRepository;
import com.domko.kanbanbackendapp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	private TaskRepository taskRepository;

	@Override
	public Optional<Task> findById(Long id) {
		return taskRepository.findById(id);
	}

	public Task saveTask(Task task){
		return taskRepository.save(task);
	}
}
