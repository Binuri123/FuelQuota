package lk.binuri.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Please enter the vehicle number.")
    @Pattern(regexp = "^[A-Za-z0-9]{1,4}-\\d{4}$", message = "Vehicle number must follow the format: ABC-1234.")
    @Column(name = "vehicle_no", unique = true, nullable = false, length = 20)
    private String vehicleNo;

    @NotBlank(message = "Please enter the chassis number.")
    @Size(min = 10, max = 60, message = "Chassis number must be between 10 and 60 characters.")
    @Column(name = "chassis_no", unique = true, nullable = false, length = 60)
    private String chassisNo;

    @NotBlank(message = "Please select the vehicle type.")
    @Pattern(regexp = "^(Car|Van|Three Wheel|Bike|Lorry|Bus)$", message = "Vehicle type must be one of: Car, Van, Three Wheel, Bike, Lorry, or Bus.")
    @Column(length = 15)
    private String type;

    @NotBlank(message = "Please select the fuel type.")
    @Pattern(regexp = "^(Petrol|Diesel)$", message = "Fuel type must be either Petrol or Diesel.")
    @Column(name = "fuel_type", nullable = false, length = 10)
    private String fuelType;

    @NotBlank(message = "Please enter the owner's NIC.")
    @Pattern(regexp = "^((\\d{12})|(\\d{9}[vVxX]))$", message = "NIC must be 12 digits or 9 digits followed by 'V' or 'X'.")
    @Column(name = "owner_nic", unique = true, nullable = false, length = 12)
    private String ownerNic;

    @NotBlank(message = "Please enter the owner's mobile number.")
    @Pattern(regexp = "^0?[^0\\D]\\d{8}$", message = "Mobile number must be 10 digits, starting with 0.")
    @Column(unique = true, nullable = false, length = 10)
    private String mobile;

    @NotBlank(message = "Please enter the owner's first name.")
    @Size(min = 3, max = 100, message = "First name must be at least 3 characters.")
    @Pattern(regexp = "^[A-Za-z'\\s]*$", message = "First name can contain only letters, spaces, and apostrophes.")
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @NotBlank(message = "Please enter the owner's last name.")
    @Size(min = 3, max = 100, message = "Last name must be at least 3 characters.")
    @Pattern(regexp = "^[A-Za-z'\\s]*$", message = "Last name can contain only letters, spaces, and apostrophes.")
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Lob
    @NotBlank(message = "Please enter the owner's address.")
    @Size(min = 5, max = 500, message = "Address must be at least 5 characters.")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;
}