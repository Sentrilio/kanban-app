package com.domko.kanbanbackendapp;

import com.domko.kanbanbackendapp.model.Team;
import com.domko.kanbanbackendapp.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
class KanbanBackendAppApplicationTests {
	private static SessionFactory sessionFactory;
	private Session session;

	@Test
	public void givenData_whenInsert_thenCreatesMtoMrelationship(){

		Team team1 = new Team();
		team1.setName("Team1");
		team1.setUsers(new HashSet<User>());
		Team team2 = new Team();
		team2.setName("Team2");
		team2.setUsers(new HashSet<User>());

		Set<Team> teams = new HashSet<>();
		teams.add(team1);
		teams.add(team2);

		User user1 = new User();
		user1.setEmail("email@op.pl");
		user1.setNickname("nickname1");
		user1.setPasswordHash("password");
		user1.setTeams(teams);
	}
}
