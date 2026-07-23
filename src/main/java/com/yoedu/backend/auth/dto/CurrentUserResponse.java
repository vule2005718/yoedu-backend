package com.yoedu.backend.auth.dto;

import com.yoedu.backend.user.UserRole;

public record CurrentUserResponse(
        Long id,
        String username,
        String fullName,
        UserRole role
) {
}