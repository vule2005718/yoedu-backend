package com.yoedu.backend.auth;

import com.yoedu.backend.auth.dto.CurrentUserResponse;
import com.yoedu.backend.auth.dto.LoginRequest;
import com.yoedu.backend.auth.dto.LoginResponse;
import com.yoedu.backend.user.User;
import com.yoedu.backend.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthService(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            JwtService jwtService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(
                        request.username(),
                        request.password()
                )
        );

        User user = findUser(request.username());
        return jwtService.generateToken(user);
    }

    public CurrentUserResponse getCurrentUser(String username) {
        User user = findUser(username);

        return new CurrentUserResponse(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getRole()
        );
    }

    private User findUser(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Invalid username or password"
                        )
                );
    }
}
