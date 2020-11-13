package com.clubtaekwondo.club.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "type_abonnement")
public class SubscriptionType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_abonnement")
    private Long id;
    @Column(name = "nbr_heur")
    private String nbrHours;

    private String description;

    @OneToMany(mappedBy = "type")
    private Set<Tariff> tariffs;

    public SubscriptionType(Long id, String nbrHours, String description, Set<Tariff> tariffs) {
        this.id = id;
        this.nbrHours = nbrHours;
        this.description = description;
        this.tariffs = tariffs;
    }

    public SubscriptionType() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long idSubscriptionType) {
        this.id = idSubscriptionType;
    }

    public String getNbrHours() {
        return nbrHours;
    }

    public void setNbrHours(String nbrHours) {
        this.nbrHours = nbrHours;
    }

    public Set<Tariff> getTariffs() {
        return tariffs;
    }

    public void setTariffs(Set<Tariff> tariffs) {
        this.tariffs = tariffs;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
