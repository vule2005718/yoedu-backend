package com.yoedu.backend.user;

import com.yoedu.backend.parent.Parent;
import com.yoedu.backend.teacher.Teacher;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "username",
            nullable = false,
            unique = true,
            length = 50
    )
    private String username;

    @Column(
            name = "password_hash",
            nullable = false,
            length = 255
    )
    private String passwordHash;

    @Column(
            name = "full_name",
            nullable = false,
            length = 100
    )
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "role",
            nullable = false,
            length = 30
    )
    private UserRole role;

    @Column(
            name = "phone",
            length = 20
    )
    private String phone;

    @Column(
            name = "email",
            length = 100
    )
    private String email;

    @Column(
            name = "is_active",
            nullable = false
    )
    private boolean active = true;

    @OneToOne(
            fetch = FetchType.LAZY,
            optional = true
    )
    @JoinColumn(
            name = "parent_id",
            unique = true,
            foreignKey = @ForeignKey(name = "fk_users_parent")
    )
    private Parent parent;

    @OneToOne(
            fetch = FetchType.LAZY,
            optional = true
    )
    @JoinColumn(
            name = "teacher_id",
            unique = true,
            foreignKey = @ForeignKey(name = "fk_users_teacher")
    )
    private Teacher teacher;

    @Column(
            name = "created_at",
            nullable = false,
            insertable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = "updated_at",
            nullable = false,
            insertable = false,
            updatable = false
    )
    private LocalDateTime updatedAt;
}
