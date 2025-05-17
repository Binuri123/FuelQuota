package lk.binuri.repository;

import lk.binuri.entity.PumpingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface PumpingLogRepository extends JpaRepository<PumpingLog, UUID> {
    @Query("""
        SELECT SUM(fp.amount)
        FROM PumpingLog fp
        WHERE fp.vehicle.vehicleNo = :vehicleNumber
          AND fp.pumpedAt BETWEEN :start AND :end
    """)
    Optional<Double> getTotalFuelPumped(
            @Param("vehicleNumber") String vehicleNumber,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
