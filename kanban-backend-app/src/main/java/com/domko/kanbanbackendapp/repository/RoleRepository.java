package com.domko.kanbanbackendapp.repository;

import com.domko.kanbanbackendapp.model.ERole;
import com.domko.kanbanbackendapp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByName(ERole name);

}
