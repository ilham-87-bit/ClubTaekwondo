package com.clubtaekwondo.club.service.imlp;

import com.clubtaekwondo.club.model.Tariff;
import com.clubtaekwondo.club.repository.TariffRepository;
import com.clubtaekwondo.club.service.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TariffImpl implements TariffService {

    @Autowired
    private TariffRepository tariffRepository;


    @Override
    public Tariff save(Tariff tariff) {
        return tariffRepository.save(tariff);
    }

    @Override
    public void delete(Tariff tariff) {
        tariffRepository.delete(tariff);
    }

    @Override
    public List<Tariff> getAllTariff() {
        return tariffRepository.findAll();
    }

    @Override
    public Tariff getTariffById(Long id) {
        return tariffRepository.getOne(id);
    }
}
