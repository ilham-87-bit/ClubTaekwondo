package com.clubtaekwondo.club.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "abonnement")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_abonnement")
    private Long idSubscription;

    @DateTimeFormat(pattern = "dd/MM/YYYY")
    @Column(name = "date_debut")
    private Date startDate;

    @DateTimeFormat(pattern = "dd/MM/YYYY")
    @Column(name = "date_fin")
    private Date endDate;

    private Boolean validation;
    private boolean newSubscription;

    @Column(name = "frais")
    private Float expenses;

    @Column(name = "prix_totale")
    private Float totalPrice;

    @Column(name = "prix_abonnement")
    private Float price;

    @OneToOne
    @JoinColumn(name = "id_categorie")
    private Categories categories;

    @OneToOne
    @JoinColumn(name = "id_type_abonnement")
    private SubscriptionType subscriptionType;

    @OneToOne
    @JoinColumn(name = "id_dure_abonnement")
    private SubscriptionPeriod subscriptionPeriod;

    @OneToOne
    @JoinColumn(name = "id_utilisateur")
    private User user;

    @OneToOne
    @JoinColumn(name = "id_eleve")
    private Student student;

    @OneToOne
    @JoinColumn(name = "id_ecole")
    private School school;

    @Column
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus subscriptionStatus = SubscriptionStatus.INITIATED;

    public Subscription(Long idSubscription, Date startDate, Date endDate, Boolean validation, Float expenses, Float totalPrice, Float price, Categories categories, SubscriptionType subscriptionType, SubscriptionPeriod subscriptionPeriod, User user, Student student, School school, SubscriptionStatus subscriptionStatus) {
        this.idSubscription = idSubscription;
        this.startDate = startDate;
        this.endDate = endDate;
        this.validation = validation;
        this.expenses = expenses;
        this.totalPrice = totalPrice;
        this.price = price;
        this.categories = categories;
        this.subscriptionType = subscriptionType;
        this.subscriptionPeriod = subscriptionPeriod;
        this.user = user;
        this.student = student;
        this.school = school;
        this.subscriptionStatus = subscriptionStatus;
    }

    public Subscription() {

    }

    public Long getIdSubscription() {
        return idSubscription;
    }

    public void setIdSubscription(Long idSubscription) {
        this.idSubscription = idSubscription;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getValidation() {
        return validation;
    }

    public void setValidation(Boolean validation) {
        this.validation = validation;
    }

    public Float getExpenses() {
        return expenses;
    }

    public void setExpenses(Float expenses) {
        this.expenses = expenses;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Categories getCategories() {
        return categories;
    }

    public void setCategories(Categories categories) {
        this.categories = categories;
    }

    public SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public SubscriptionPeriod getSubscriptionPeriod() {
        return subscriptionPeriod;
    }

    public void setSubscriptionPeriod(SubscriptionPeriod subscriptionPeriod) {
        this.subscriptionPeriod = subscriptionPeriod;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public SubscriptionStatus getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(SubscriptionStatus subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public boolean isNewSubscription() {
        return newSubscription;
    }

    public void setNewSubscription(boolean newSubscription) {
        this.newSubscription = newSubscription;
    }
}
