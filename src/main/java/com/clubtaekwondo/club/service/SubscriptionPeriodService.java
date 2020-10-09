package com.clubtaekwondo.club.service;


import com.clubtaekwondo.club.model.Categories;
import com.clubtaekwondo.club.model.SubscriptionPeriod;

import java.util.List;

public interface SubscriptionPeriodService {

    SubscriptionPeriod save(SubscriptionPeriod subscriptionPeriod);

    void delete(SubscriptionPeriod subscriptionPeriod);

    List<SubscriptionPeriod> getAllPeriod();

    SubscriptionPeriod findById(Long id);
}
