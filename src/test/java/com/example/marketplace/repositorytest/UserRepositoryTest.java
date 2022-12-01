package com.example.marketplace.repositorytest;

import com.example.marketplace.model.User;
import com.example.marketplace.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void createUser() {
        User user = new User("john@example.com", "John", "Doe", "password", "USER");
        userRepository.save(user);

        List<User> foundUsers = userRepository.findAll();
        assertThat(foundUsers).hasSize(1);
    }

    @Test
    void deleteUser() {
        User user = new User("john@example.com", "John", "Doe", "password", "USER");
        userRepository.save(user);
        userRepository.delete(user);

        List<User> foundUsers = userRepository.findAll();
        assertThat(foundUsers).hasSize(0);
    }

    @Test
    void findUserByEmail() {
        User user = new User("john@example.com", "John", "Doe", "password", "USER");
        userRepository.save(user);

        User foundUser = userRepository.findByEmail("john@example.com");
        assertThat(foundUser).isEqualTo(user);
    }

}

