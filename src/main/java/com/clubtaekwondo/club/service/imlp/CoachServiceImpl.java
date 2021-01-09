package com.clubtaekwondo.club.service.imlp;

import com.clubtaekwondo.club.model.Coach;
import com.clubtaekwondo.club.model.User;
import com.clubtaekwondo.club.repository.CoachRepository;
import com.clubtaekwondo.club.repository.UserRepository;
import com.clubtaekwondo.club.service.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoachServiceImpl implements CoachService {

    @Autowired
    private CoachRepository coachRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Coach saveCoach(Coach coach) {
        return coachRepository.save(coach);
    }

    @Override
    public void deleteCoach(Coach coach) {
        coachRepository.delete(coach);
    }

    @Override
    public List<Coach> getAllCoach() {
        return coachRepository.findAll();
    }

    @Override
    public Coach findById(Long id) {
        return coachRepository.getOne(id);
    }
}
