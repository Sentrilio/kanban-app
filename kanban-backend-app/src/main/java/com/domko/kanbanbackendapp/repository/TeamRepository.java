package com.domko.kanbanbackendapp.repository;

import com.domko.kanbanbackendapp.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository  extends JpaRepository<Team, Long> {
}
