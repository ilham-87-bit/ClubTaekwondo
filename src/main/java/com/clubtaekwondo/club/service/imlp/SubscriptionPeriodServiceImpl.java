package com.clubtaekwondo.club.service.imlp;

import com.clubtaekwondo.club.model.SubscriptionPeriod;
import com.clubtaekwondo.club.repository.SubscriptionPeriodRepository;
import com.clubtaekwondo.club.service.SubscriptionPeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionPeriodServiceImpl implements SubscriptionPeriodService {
    @Autowired
    private SubscriptionPeriodRepository subs;


    @Override
    public SubscriptionPeriod save(SubscriptionPeriod subscriptionPeriod) {
        return subs.save(subscriptionPeriod);
    }

    @Override
    public void delete(SubscriptionPeriod subscriptionPeriod) {
        subs.delete(subscriptionPeriod);
    }

    @Override
    public List<SubscriptionPeriod> getAllPeriod() {
        return subs.findAll();
    }

    @Override
    public SubscriptionPeriod findById(Long id) {
        return subs.getOne(id);
    }
}
