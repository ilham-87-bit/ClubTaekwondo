package com.clubtaekwondo.club.repository;

import com.clubtaekwondo.club.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TariffRepository extends JpaRepository<Tariff, TariffPK> {

    Tariff findTariffByTariffPK(TariffPK tariffPK);
}
