package com.domko.kanbanbackendapp.repository;

import com.domko.kanbanbackendapp.model.UserTeam;
import com.domko.kanbanbackendapp.model.UserTeamKey;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserTeamRepository extends JpaRepository<UserTeam, UserTeamKey> {

	@Override
	Optional<UserTeam> findById(UserTeamKey userTeamKey);

	@Query(value = "SELECT * FROM public.user_team ut WHERE ut.user_id = :userId", nativeQuery = true)
	List<UserTeam> findAllTeamsOfUser(@Param("userId") Long userId);

	@Override
	List<UserTeam> findAll();

	@Override
	<S extends UserTeam> List<S> findAll(Example<S> example);
}
