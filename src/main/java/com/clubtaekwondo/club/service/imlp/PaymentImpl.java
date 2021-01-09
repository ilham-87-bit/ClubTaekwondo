package com.clubtaekwondo.club.service.imlp;

import com.clubtaekwondo.club.model.Payment;
import com.clubtaekwondo.club.model.Subscription;
import com.clubtaekwondo.club.repository.PaymentRepository;
import com.clubtaekwondo.club.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public List<Payment> findByCode(String code) {
        return paymentRepository.findByCode(code);
    }

    @Override
    public Payment createNewPayment(List<Subscription> subscriptions) {
        Payment payment = new Payment();
        UUID uuid = UUID.randomUUID();
        payment.setSubscriptionList(subscriptions);
        payment.setCode(uuid.toString());
        return paymentRepository.save(payment);
    }

    @Override
    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public Payment findById(Long id) {
        return paymentRepository.getOne(id);
    }
}
