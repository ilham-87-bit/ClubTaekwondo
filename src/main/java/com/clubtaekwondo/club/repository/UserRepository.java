package com.clubtaekwondo.club.repository;

import com.clubtaekwondo.club.model.User;
import com.clubtaekwondo.club.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("select COUNT (id) from User where userRole = ?1 ")
    int getUserCountWithRole(UserRole role);

    User findByEmail(String email);
}
