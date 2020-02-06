package com.domko.kanbanbackendapp;

import com.domko.kanbanbackendapp.model.User;
import com.domko.kanbanbackendapp.repository.BoardRepository;
import com.domko.kanbanbackendapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;


    @Test
    public void givenUser_whenFindByUsername_thenReturnUser() {
        //given
        User user = new User("user", "user@email.com", "password");
        entityManager.persist(user);
        entityManager.flush();
        //when
        Optional<User> found = userRepository.findByUsername(user.getUsername());
        //then
        assertThat(found.isPresent()).isEqualTo(true);
        assertThat(found.get().getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    public void givenUser_whenFindByEmail_thenReturnUser() {
        //given
        User user = new User("user", "user@email.com", "password");
        entityManager.persist(user);
        entityManager.flush();
        //when
        Optional<User> found = userRepository.findByEmail(user.getEmail());
        //then
        assertThat(found.isPresent()).isEqualTo(true);
        assertThat(found.get().getEmail()).isEqualTo(user.getEmail());
    }

    public void givenNothing_whenFindByUsername_thenReturnOptionalEmpty() {
        Optional<User> found = userRepository.findByUsername("username");
        assertThat(found.isEmpty()).isEqualTo(true);
    }
}
