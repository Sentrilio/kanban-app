package com.domko.kanbanbackendapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KanbanBackendAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(KanbanBackendAppApplication.class, args);
	}

}
