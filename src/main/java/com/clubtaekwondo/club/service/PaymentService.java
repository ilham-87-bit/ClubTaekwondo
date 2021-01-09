package com.clubtaekwondo.club.service;

import com.clubtaekwondo.club.model.Payment;
import com.clubtaekwondo.club.model.Subscription;

import java.util.List;

public interface PaymentService {

    List<Payment> findByCode(String code);

    Payment createNewPayment(List<Subscription> subscriptions);

    Payment save(Payment payment);

    Payment findById(Long id);
}
