package com.clubtaekwondo.club.repository;

import com.clubtaekwondo.club.model.Subscription;
import com.clubtaekwondo.club.model.SubscriptionStatus;
import com.clubtaekwondo.club.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByUserAndSubscriptionStatus(User user, SubscriptionStatus status);
}
