package com.clubtaekwondo.club.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "type_abonnement")
public class SubscriptionType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_type_abonnement")
    private Long idSubscriptionType;
    @Column(name = "nbr_heur")
    private Integer nbrHours;

    @OneToMany(mappedBy = "type")
    private Set<Tariff> tariffs;

    public SubscriptionType(Long idSubscriptionType, Integer nbrHours) {
        this.idSubscriptionType = idSubscriptionType;
        this.nbrHours = nbrHours;
    }

    public SubscriptionType() {

    }

    public Long getIdSubscriptionType() {
        return idSubscriptionType;
    }

    public void setIdSubscriptionType(Long idSubscriptionType) {
        this.idSubscriptionType = idSubscriptionType;
    }

    public Integer getNbrHours() {
        return nbrHours;
    }

    public void setNbrHours(Integer nbrHours) {
        this.nbrHours = nbrHours;
    }
    public Set<Tariff> getTarifs() {
        return tariffs;
    }

    public void setTarifs(Set<Tariff> tariffs) {
        this.tariffs = tariffs;
    }
}
