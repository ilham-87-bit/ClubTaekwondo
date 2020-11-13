package com.clubtaekwondo.club.model;


import javax.persistence.*;

@Entity
@Table(name = "tarif")
public class Tariff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarif")
    private Long idTarif;

    @ManyToOne
    @JoinColumn(name="id_categorie")
   private Categories category ;
    @ManyToOne
    @JoinColumn(name="id_type_abonnement")
   private SubscriptionType type ;
    @ManyToOne
    @JoinColumn(name="id_dure_abonnement")
   private SubscriptionPeriod period ;

    private float prix ;

    public Tariff(Long idTarif, Categories category, SubscriptionType type, SubscriptionPeriod period, float prix) {
        this.idTarif = idTarif;
        this.category = category;
        this.type = type;
        this.period = period;
        this.prix = prix;
    }

    public Tariff() {
    }

    public Long getIdTarif() {
        return idTarif;
    }

    public void setIdTarif(Long idTarif) {
        this.idTarif = idTarif;
    }

    }
