package com.clubtaekwondo.club.service;

import com.clubtaekwondo.club.model.Subscription;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SubscriptionService {

    Subscription save(Subscription subscription);

    void delete(Subscription subscription);

    Subscription findById(Long id);

    List<Subscription> getAllSubscription();

}
