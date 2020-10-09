package com.clubtaekwondo.club.repository;

import com.clubtaekwondo.club.model.SubscriptionPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionPeriodRepository extends JpaRepository<SubscriptionPeriod, Long> {

    @Override
    List<SubscriptionPeriod> findAll();
}
