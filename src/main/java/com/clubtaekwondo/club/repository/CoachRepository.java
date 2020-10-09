package com.clubtaekwondo.club.repository;

import com.clubtaekwondo.club.model.Coach;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoachRepository extends JpaRepository<Coach, Long> {
}
