package com.domko.kanbanbackendapp.repository;

import com.domko.kanbanbackendapp.model.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BoardRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private BoardRepository boardRepository;


    @Test
    public void givenBoard_whenFindById_thenReturnBoard() {
        //given
        Team team = new Team("Team1");
        Board board = new Board();
        board.setName("Board 1");
        board.setCreateDate(new Date());
        board.setTeam(team);
        System.out.println("id before: " + board.getId());// before persists its null

        entityManager.persist(team);
        entityManager.persist(board);
        entityManager.flush();
        //when
        Optional<Board> found = boardRepository.findById(board.getId());
        //then
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getName()).isEqualTo(board.getName());
        System.out.println("id: " + found.get().getId());//after persist its 1
    }

    @Test
    public void given3Boards_whenFindAll_thenReturnListOf3Boards() {
        //given
        Team team = new Team("Team1");
        Board board1 = new Board();
        board1.setName("Board 1");
        board1.setCreateDate(new Date());
        board1.setTeam(team);
        Board board2 = new Board();
        board2.setName("Board 3");
        board2.setCreateDate(new Date());
        board2.setTeam(team);
        Board board3 = new Board();
        board3.setName("Board 3");
        board3.setCreateDate(new Date());
        board3.setTeam(team);

        entityManager.persist(team);
        entityManager.persist(board1);
        entityManager.persist(board2);
        entityManager.persist(board3);
        entityManager.flush();
        //when
        List<Board> boards = boardRepository.findAll();
        //then
        assertThat(boards.size()).isEqualTo(3);
        assertThat(boards).contains(board1,board2,board3);
    }


}
