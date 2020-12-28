package com.clubtaekwondo.club.repository;

import com.clubtaekwondo.club.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole,Long> {

    UserRole findByRole(String role);
}
