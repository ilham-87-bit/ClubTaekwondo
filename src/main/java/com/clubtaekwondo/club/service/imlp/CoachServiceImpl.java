package com.clubtaekwondo.club.service.imlp;

import com.clubtaekwondo.club.model.Coach;
import com.clubtaekwondo.club.repository.CoachRepository;
import com.clubtaekwondo.club.service.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoachServiceImpl implements CoachService {

    @Autowired
    private CoachRepository coachRepository;

    @Override
    public Coach saveCoach(Coach coach) {
        return coachRepository.save(coach);
    }

    @Override
    public Coach updateCoach(Coach coach) {
        return null;
    }

    @Override
    public void deleteCoach(Coach coach) {
        coachRepository.delete(coach);
    }
}
