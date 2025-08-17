package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("findByEmail doit retourner l'utilisateur si email existe")
    void testFindByEmail() {
        User user = new User();
        user.setEmail("test@email.com");
        userRepository.save(user);

        Optional<User> found = userRepository.findByEmail("test@email.com");
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@email.com");
    }

    @Test
    @DisplayName("existsByEmail doit retourner true si email existe")
    void testExistsByEmail() {
        User user = new User();
        user.setEmail("exists@email.com");
        userRepository.save(user);

        boolean exists = userRepository.existsByEmail("exists@email.com");
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("existsByEmail doit retourner false si email n'existe pas")
    void testExistsByEmailFalse() {
        boolean exists = userRepository.existsByEmail("notfound@email.com");
        assertThat(exists).isFalse();
    }
}