package com.domko.kanbanbackendapp.repository;

import com.domko.kanbanbackendapp.model.UserTeam;
import com.domko.kanbanbackendapp.model.UserTeamKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTeamRepository extends JpaRepository<UserTeam, UserTeamKey> {

}
