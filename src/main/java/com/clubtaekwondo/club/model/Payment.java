package com.clubtaekwondo.club.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String code;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.INITIATED;


    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "abonnement_payement",
            joinColumns = {@JoinColumn(name = "id_payement")},
            inverseJoinColumns = {@JoinColumn(name = "id_abonnement")})
    private List<Subscription> subscriptionList;

    public Payment(Long id, String code, PaymentStatus paymentStatus) {
        this.id = id;
        this.code = code;
        this.paymentStatus = paymentStatus;
    }

    public Payment() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public List<Subscription> getSubscriptionList() {
        return subscriptionList;
    }

    public void setSubscriptionList(List<Subscription> subscriptionList) {
        this.subscriptionList = subscriptionList;
    }
}
