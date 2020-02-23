package com.domko.kanbanbackendapp.repository;

import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.model.Team;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TeamRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private TeamRepository teamRepository;

    @Test
    public void givenTeam_whenFindById_thenReturnTeam() {
        //given
        Team team = new Team("Team1");

        entityManager.persist(team);
        entityManager.flush();
        //when
        Optional<Team> found = teamRepository.findById(team.getId());
        //then
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getName()).isEqualTo(team.getName());
    }

    @Test
    public void given3Teams_whenFindAll_thenReturn3Teams() {
        //given
        Team team1 = new Team("Team1");
        Team team2 = new Team("Team2");
        Team team3 = new Team("Team3");

        entityManager.persist(team1);
        entityManager.persist(team2);
        entityManager.persist(team3);
        entityManager.flush();
        //when
        List<Team> allTeams = teamRepository.findAll();
        //then
        assertThat(allTeams.size()).isEqualTo(3);
        assertThat(allTeams).contains(team1,team2,team3);
    }

    @Test
    public void givenTeam_whenExistsById_thenReturnTrue() {
        //given
        Team team = new Team("Team1");

        entityManager.persist(team);
        entityManager.flush();
        //when
        Boolean found = teamRepository.existsById(team.getId());
        //then
        assertThat(found).isTrue();
    }
}
