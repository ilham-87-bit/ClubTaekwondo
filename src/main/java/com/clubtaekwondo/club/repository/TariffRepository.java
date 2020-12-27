package com.clubtaekwondo.club.repository;

import com.clubtaekwondo.club.model.Categories;
import com.clubtaekwondo.club.model.SubscriptionPeriod;
import com.clubtaekwondo.club.model.SubscriptionType;
import com.clubtaekwondo.club.model.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TariffRepository extends JpaRepository<Tariff, Long> {
    @Query(value = "select t from Tariff t, Categories  c, SubscriptionPeriod sp , SubscriptionType st where t.category.idCategory=c.idCategory and t.period.id=sp.id and t.type.idType=st.idType")
    Tariff getOneTariff(Categories categories, SubscriptionPeriod subscriptionPeriod, SubscriptionType subscriptionType);
}
