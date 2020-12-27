package com.clubtaekwondo.club.service.imlp;

import com.clubtaekwondo.club.model.Subscription;
import com.clubtaekwondo.club.model.SubscriptionStatus;
import com.clubtaekwondo.club.model.User;
import com.clubtaekwondo.club.repository.SubscriptionRepository;
import com.clubtaekwondo.club.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;


    @Override
    public Subscription save(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    @Override
    public void delete(Subscription subscription) {
        subscriptionRepository.delete(subscription);
    }

    @Override
    public Subscription findById(Long id) {
        return subscriptionRepository.getOne(id);
    }

    @Override
    public List<Subscription> getAllSubscription() {
        return subscriptionRepository.findAll();
    }

    @Override
    public List<Subscription> getCart() {
        // get the conected user
        Object authentication = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (authentication instanceof User) {
            return subscriptionRepository.findByUserAndSubscriptionStatus((User) authentication, SubscriptionStatus.CHART);
        }
        return Collections.emptyList();
    }
}
