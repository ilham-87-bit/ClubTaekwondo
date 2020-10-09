package com.clubtaekwondo.club.repository;

import com.clubtaekwondo.club.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
}
