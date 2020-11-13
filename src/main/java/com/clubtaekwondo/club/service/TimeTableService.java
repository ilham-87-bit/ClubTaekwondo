package com.clubtaekwondo.club.service;

import com.clubtaekwondo.club.model.TimeTable;

import java.util.List;

public interface TimeTableService {

    TimeTable save(TimeTable timeTable);

    void delete(TimeTable timeTable);

    List<TimeTable> getAllTimeTable();

    TimeTable getTimeTableById(Long id);
}
