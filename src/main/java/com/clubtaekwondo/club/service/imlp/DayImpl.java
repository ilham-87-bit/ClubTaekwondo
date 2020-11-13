package com.clubtaekwondo.club.service.imlp;

import com.clubtaekwondo.club.model.Day;
import com.clubtaekwondo.club.repository.DayRepository;
import com.clubtaekwondo.club.service.DayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DayImpl implements DayService {

    @Autowired
    private DayRepository dayRepository;


    @Override
    public List<Day> getAllDay() {
        return dayRepository.findAll();
    }

    @Override
    public Day getDayById(Long id) {
        return dayRepository.getOne(id);
    }
}
