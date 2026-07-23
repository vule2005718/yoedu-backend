package com.yoedu.backend.auth;

import com.yoedu.backend.auth.dto.CurrentUserResponse;
import com.yoedu.backend.auth.dto.LoginRequest;
import com.yoedu.backend.auth.dto.LoginResponse;
import com.yoedu.backend.user.User;
import com.yoedu.backend.user.UserRepository;
import com.yoedu.backend.user.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldLoginAndReturnToken() {
        LoginRequest request = new LoginRequest(
                "admin",
                "Password123"
        );

        User user = activeAdmin();
        LoginResponse expected =
                new LoginResponse("signed-jwt", "Bearer", 3600);

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(userRepository.findByUsername(request.username()))
                .thenReturn(Optional.of(user));
        when(jwtService.generateToken(user))
                .thenReturn(expected);

        LoginResponse actual = authService.login(request);

        assertSame(expected, actual);
        verify(authenticationManager)
                .authenticate(any(Authentication.class));
        verify(jwtService).generateToken(user);
    }

    @Test
    void shouldRejectInvalidCredentials() {
        LoginRequest request = new LoginRequest(
                "admin",
                "wrong-password"
        );

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new BadCredentialsException(
                        "Invalid username or password"
                ));

        assertThrows(
                BadCredentialsException.class,
                () -> authService.login(request)
        );

        verifyNoInteractions(userRepository, jwtService);
    }

    @Test
    void shouldReturnCurrentUser() {
        User user = activeAdmin();

        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));

        CurrentUserResponse response =
                authService.getCurrentUser(user.getUsername());

        assertEquals(user.getId(), response.id());
        assertEquals(user.getUsername(), response.username());
        assertEquals(user.getFullName(), response.fullName());
        assertEquals(UserRole.ADMIN, response.role());
    }

    private User activeAdmin() {
        User user = new User();
        user.setId(10L);
        user.setUsername("admin");
        user.setPasswordHash("bcrypt-hash");
        user.setFullName("Administrator");
        user.setRole(UserRole.ADMIN);
        user.setActive(true);
        return user;
    }
}
