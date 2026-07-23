package com.yoedu.backend.user.dto;

import com.yoedu.backend.user.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(

        @NotBlank
        @Size(min = 3, max = 50)
        String username,

        @NotBlank
        @Size(min = 8, max = 72)
        String password,

        @NotBlank
        @Size(max = 100)
        String fullName,

        @Size(max = 20)
        String phone,

        @Email
        @Size(max = 100)
        String email,

        @NotNull
        UserRole role
) {
}