package lk.binuri.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(name = "vehicle_no", unique = true, nullable = false, length = 20)
    private String vehicleNo;

    @Column(name = "chassis_no", unique = true, nullable = false, length = 60)
    private String chassisNo;

    @Column(length = 15)
    private String type;

    @Column(name = "fuel_type", nullable = false, length = 10)
    private String fuelType;

    @Column(name = "owner_nic", unique = true, nullable = false, length = 12)
    private String ownerNic;

    @Column(unique = true, nullable = false, length = 10)
    private String mobile;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;

    @Lob
    @JsonIgnore
    @Column(name = "qr", columnDefinition = "MEDIUMBLOB")
    private byte[] qr;

    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @OneToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private User user;
}