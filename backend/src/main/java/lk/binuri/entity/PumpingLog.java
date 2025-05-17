package lk.binuri.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "pumping_log")
public class PumpingLog {
    @Id
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "fuel_station_id", nullable = false)
    private FuelStation fuelStation;

    private Double amount;

    @Column(name = "pumped_at")
    private LocalDateTime pumpedAt;
}
