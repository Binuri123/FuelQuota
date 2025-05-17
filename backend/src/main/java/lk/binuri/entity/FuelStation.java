package lk.binuri.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "fuel_station")
public class FuelStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false,unique = true, length = 25)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String dealer;

    @Column(nullable = false, length = 50)
    private String city;

    @Column(nullable = false, length = 10)
    private String contact1;

    @Column(length = 10)
    private String contact2;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;

    @Column(nullable = false, length = 20)
    private String company;

    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @OneToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private User user;
}
