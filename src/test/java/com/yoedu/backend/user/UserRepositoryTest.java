package com.yoedu.backend.user;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldSaveAndFindUserByUsername() {
        String username =
                "repo_test_" + UUID.randomUUID()
                        .toString()
                        .substring(0, 8);
        assertFalse(userRepository.existsByUsername(username));

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash("test-password-hash");
        user.setFullName("Repository Test User");
        user.setRole(UserRole.ADMIN);

        User savedUser = userRepository.saveAndFlush(user);
        assertTrue(userRepository.existsByUsername(username));

        assertNotNull(savedUser.getId());

        entityManager.clear();

        Optional<User> result =
                userRepository.findByUsername(username);

        assertTrue(result.isPresent());

        User foundUser = result.get();

        assertEquals(username, foundUser.getUsername());
        assertEquals(UserRole.ADMIN, foundUser.getRole());
        assertTrue(foundUser.isActive());
        assertNotNull(foundUser.getCreatedAt());
        assertNotNull(foundUser.getUpdatedAt());
    }
}