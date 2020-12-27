package com.clubtaekwondo.club.service;

import com.clubtaekwondo.club.model.Categories;
import com.clubtaekwondo.club.model.SubscriptionPeriod;
import com.clubtaekwondo.club.model.SubscriptionType;
import com.clubtaekwondo.club.model.Tariff;

import java.util.List;

public interface TariffService {

    Tariff save(Tariff tariff);

    void delete(Tariff tariff);

    List<Tariff> getAllTariff();

    Tariff getTariffById(Long id);

    Tariff getOneTariff(Categories categories, SubscriptionPeriod subscriptionPeriod, SubscriptionType subscriptionType);
}
