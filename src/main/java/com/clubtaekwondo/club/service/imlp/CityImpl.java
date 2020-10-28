package com.clubtaekwondo.club.service.imlp;

import com.clubtaekwondo.club.model.City;
import com.clubtaekwondo.club.repository.CityRepository;
import com.clubtaekwondo.club.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityImpl implements CityService {

    @Autowired
    private CityRepository cityRepository;

    @Override
    public City save(City city) {
        return cityRepository.save(city);
    }

    @Override
    public List<City> getAllCity() {
        return cityRepository.findAll();
    }

    @Override
    public City findById(Long id) {
        return cityRepository.getOne(id);
    }

    @Override
    public void delete(City city) {
        cityRepository.delete(city);
    }
}
