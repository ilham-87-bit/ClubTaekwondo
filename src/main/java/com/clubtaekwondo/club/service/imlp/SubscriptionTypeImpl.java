package com.clubtaekwondo.club.service.imlp;

import com.clubtaekwondo.club.model.SubscriptionType;
import com.clubtaekwondo.club.repository.SubscriptionTypeRepository;
import com.clubtaekwondo.club.service.SubscriptionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionTypeImpl implements SubscriptionTypeService {

    @Autowired
    private SubscriptionTypeRepository subscriptionTypeRepository;

    @Override
    public SubscriptionType save(SubscriptionType subscriptionType) {
        return subscriptionTypeRepository.save(subscriptionType);
    }

    @Override
    public void delete(SubscriptionType subscriptionType) {
        subscriptionTypeRepository.delete(subscriptionType);
    }

    @Override
    public List<SubscriptionType> getAllSubscriptionType() {
        return subscriptionTypeRepository.findAll();
    }

    @Override
    public SubscriptionType findById(Long id) {
        return subscriptionTypeRepository.getOne(id);
    }
}
