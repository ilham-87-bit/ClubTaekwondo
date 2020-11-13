package com.clubtaekwondo.club.service.imlp;

import com.clubtaekwondo.club.model.TimeTable;
import com.clubtaekwondo.club.repository.TimeTableRepository;
import com.clubtaekwondo.club.service.TimeTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeTableImpl implements TimeTableService {

    @Autowired
    private TimeTableRepository timeTableRepository;

    @Override
    public TimeTable save(TimeTable timeTable) {
        return timeTableRepository.save(timeTable);
    }

    @Override
    public void delete(TimeTable timeTable) {
        timeTableRepository.delete(timeTable);
    }

    @Override
    public List<TimeTable> getAllTimeTable() {
        return timeTableRepository.findAll();
    }

    @Override
    public TimeTable getTimeTableById(Long id) {
        return timeTableRepository.getOne(id);
    }
}
