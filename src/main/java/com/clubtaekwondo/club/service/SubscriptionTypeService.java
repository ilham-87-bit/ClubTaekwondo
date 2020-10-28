package com.clubtaekwondo.club.service;

import com.clubtaekwondo.club.model.SubscriptionType;

import java.util.List;

public interface SubscriptionTypeService {

    SubscriptionType save(SubscriptionType subscriptionType);

    void delete(SubscriptionType subscriptionType);

    List<SubscriptionType> getAllSubscriptionType();

    SubscriptionType findById(Long id);
}
