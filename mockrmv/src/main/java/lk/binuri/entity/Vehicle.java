package lk.binuri.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "vehicle_no",unique = true,nullable = false,length = 20)
    private String vehicleNo;

    @Column(name = "chassis_no",unique = true,nullable = false,length = 60)
    private String chassisNo;

    @Column(length = 15)
    private String type;
}
