package lk.binuri.repository;

import lk.binuri.entity.FuelStation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuelStationRepository extends JpaRepository<FuelStation,Integer> {
    FuelStation findByCode(String code);
}
