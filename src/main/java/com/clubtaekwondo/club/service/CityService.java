package com.clubtaekwondo.club.service;

import com.clubtaekwondo.club.model.City;

import java.util.List;

public interface CityService {

    City save(City city);

    List<City> getAllCity();

    City findById(Long id);

    void delete(City city);


}
