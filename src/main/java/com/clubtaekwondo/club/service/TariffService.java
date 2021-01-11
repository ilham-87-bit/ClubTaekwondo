package com.clubtaekwondo.club.service;

import com.clubtaekwondo.club.model.*;

import java.util.List;

public interface TariffService {

    Tariff save(Tariff tariff);

    void delete(Tariff tariff);

    List<Tariff> getAllTariff();

    Tariff getOneTariff(TariffPK tariffPK);
}
