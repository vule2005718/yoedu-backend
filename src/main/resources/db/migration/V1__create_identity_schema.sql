CREATE TABLE parents (
    id BIGINT NOT NULL AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    address VARCHAR(255),
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
        ON UPDATE CURRENT_TIMESTAMP(6),

    CONSTRAINT pk_parents PRIMARY KEY (id),
    CONSTRAINT uk_parents_phone UNIQUE (phone)
);

CREATE TABLE teachers (
    id BIGINT NOT NULL AUTO_INCREMENT,
    teacher_code VARCHAR(20) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    teacher_role VARCHAR(20) NOT NULL DEFAULT 'TEACHER',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
        ON UPDATE CURRENT_TIMESTAMP(6),

    CONSTRAINT pk_teachers PRIMARY KEY (id),
    CONSTRAINT uk_teachers_code UNIQUE (teacher_code),
    CONSTRAINT uk_teachers_phone UNIQUE (phone),
    CONSTRAINT chk_teachers_role
        CHECK (teacher_role IN ('TEACHER', 'ASSISTANT', 'BOTH'))
);

CREATE TABLE students (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_code VARCHAR(20) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    date_of_birth DATE,
    gender VARCHAR(10) NOT NULL DEFAULT 'OTHER',
    grade_level VARCHAR(30),
    school_name VARCHAR(100),
    phone VARCHAR(20),
    parent_id BIGINT,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    latest_score DECIMAL(5, 2),
    note VARCHAR(500),
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
        ON UPDATE CURRENT_TIMESTAMP(6),

    CONSTRAINT pk_students PRIMARY KEY (id),
    CONSTRAINT uk_students_code UNIQUE (student_code),
    CONSTRAINT chk_students_gender
      CHECK (gender IN ('MALE', 'FEMALE', 'OTHER')),
    CONSTRAINT chk_students_status
      CHECK (status IN ('ACTIVE', 'PAUSED', 'DROPPED')),
    CONSTRAINT chk_students_score
      CHECK (latest_score IS NULL OR latest_score BETWEEN 0 AND 10),
    CONSTRAINT fk_students_parent
      FOREIGN KEY (parent_id) REFERENCES parents(id),

    INDEX idx_students_parent_id (parent_id)
);

CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    role VARCHAR(30) NOT NULL,
    parent_id BIGINT,
    teacher_id BIGINT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
        ON UPDATE CURRENT_TIMESTAMP(6),

    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uk_users_username UNIQUE (username),
    CONSTRAINT uk_users_parent UNIQUE (parent_id),
    CONSTRAINT uk_users_teacher UNIQUE (teacher_id),
    CONSTRAINT chk_users_role
       CHECK (role IN ('ADMIN', 'ACADEMIC_STAFF', 'CASHIER', 'PARENT')),
    CONSTRAINT fk_users_parent
       FOREIGN KEY (parent_id) REFERENCES parents(id),
    CONSTRAINT fk_users_teacher
       FOREIGN KEY (teacher_id) REFERENCES teachers(id),

    INDEX idx_users_role (role)
);