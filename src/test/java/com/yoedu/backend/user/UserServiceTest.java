package com.yoedu.backend.user;

import com.yoedu.backend.user.dto.CreateUserRequest;
import com.yoedu.backend.user.dto.UserResponse;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldCreateUserWithEncodedPassword() {
        CreateUserRequest request = new CreateUserRequest(
                "new_admin",
                "Password123",
                "New Administrator",
                "0901234567",
                "admin@yoedu.vn",
                UserRole.ADMIN
        );

        when(userRepository.existsByUsername(request.username()))
                .thenReturn(false);

        when(passwordEncoder.encode(request.password()))
                .thenReturn("bcrypt-password-hash");

        when(userRepository.saveAndFlush(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    user.setId(1L);
                    user.setCreatedAt(LocalDateTime.now());
                    user.setUpdatedAt(LocalDateTime.now());
                    return user;
                });

        UserResponse response = userService.createUser(request);

        ArgumentCaptor<User> userCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository).saveAndFlush(userCaptor.capture());
        verify(entityManager).refresh(any(User.class));

        User savedUser = userCaptor.getValue();

        assertEquals("bcrypt-password-hash", savedUser.getPasswordHash());
        assertEquals(request.username(), response.username());
        assertEquals(request.fullName(), response.fullName());
        assertEquals(UserRole.ADMIN, response.role());
        assertTrue(response.active());
    }

    @Test
    void shouldRejectDuplicateUsername() {
        CreateUserRequest request = new CreateUserRequest(
                "existing_user",
                "Password123",
                "Existing User",
                null,
                null,
                UserRole.ADMIN
        );

        when(userRepository.existsByUsername(request.username()))
                .thenReturn(true);

        assertThrows(
                DuplicateUsernameException.class,
                () -> userService.createUser(request)
        );

        verifyNoInteractions(passwordEncoder);

        verify(userRepository, never())
                .saveAndFlush(any(User.class));
    }
}