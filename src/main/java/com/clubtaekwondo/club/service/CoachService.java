package com.clubtaekwondo.club.service;

import com.clubtaekwondo.club.model.Coach;
import com.clubtaekwondo.club.model.User;

import java.util.List;

public interface CoachService {

    Coach saveCoach(Coach coach);

    void deleteCoach(Coach coach);

    List<Coach> getAllCoach();

    Coach findById(Long id);
}
