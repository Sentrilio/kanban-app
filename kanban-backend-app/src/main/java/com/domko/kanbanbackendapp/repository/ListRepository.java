package com.domko.kanbanbackendapp.repository;

import com.domko.kanbanbackendapp.model.BColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListRepository extends JpaRepository<BColumn,Long> {

}
