package com.clubtaekwondo.club.service;

import com.clubtaekwondo.club.model.Coach;

public interface CoachService {

    Coach saveCoach(Coach coach);

    Coach updateCoach(Coach coach);

    void deleteCoach(Coach coach);
}
