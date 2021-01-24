package com.clubtaekwondo.club.model;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tarif")
public class Tariff implements Serializable {

    @EmbeddedId
    private TariffPK tariffPK;

    private float prix;

    private boolean isExist = false;

    public Tariff(TariffPK tariffPK, float prix) {
        this.tariffPK = tariffPK;
        this.prix = prix;
    }

    public Tariff() {
    }

    public Tariff(TariffPK tariffPK, float prix, boolean isExist) {
        this.tariffPK = tariffPK;
        this.prix = prix;
        this.isExist = isExist;
    }

    public TariffPK getTariffPK() {
        return tariffPK;
    }

    public void setTariffPK(TariffPK tariffPK) {
        this.tariffPK = tariffPK;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public boolean isExist() {
        return isExist;
    }

    public void setExist(boolean exist) {
        isExist = exist;
    }

    @Override
    public String toString() {
        return "model.Tarif[ id=" + this.getTariffPK() + " ]";
    }
}
