package com.clubtaekwondo.club.service;

import com.clubtaekwondo.club.model.Day;

import java.util.List;

public interface DayService {

    List<Day> getAllDay();

    Day getDayById(Long id);
}
