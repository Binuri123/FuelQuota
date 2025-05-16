package lk.binuri.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lk.binuri.util.PasswordMatches;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PasswordMatches(passwordField = "password", confirmPasswordField = "confirmPassword")
public class VehicleRegisterRequestDTO {

    @NotBlank(message = "Please enter the vehicle number.")
    @Pattern(regexp = "^[A-Za-z0-9]{1,4}-\\d{4}$", message = "Vehicle number must follow the format: ABC-1234.")
    private String vehicleNo;

    @NotBlank(message = "Please enter the chassis number.")
    @Size(min = 10, max = 60, message = "Chassis number must be between 10 and 60 characters.")
    private String chassisNo;

    @NotBlank(message = "Please select the vehicle type.")
    @Pattern(regexp = "^(Car|Van|Three Wheel|Bike|Lorry|Bus)$", message = "Vehicle type must be one of: Car, Van, Three Wheel, Bike, Lorry, or Bus.")
    private String type;

    @NotBlank(message = "Please select the fuel type.")
    @Pattern(regexp = "^(Petrol|Diesel)$", message = "Fuel type must be either Petrol or Diesel.")
    private String fuelType;

    @NotBlank(message = "Please enter the owner's NIC.")
    @Pattern(regexp = "^((\\d{12})|(\\d{9}[vVxX]))$", message = "NIC must be 12 digits or 9 digits followed by 'V' or 'X'.")
    private String ownerNic;

    @NotBlank(message = "Please enter the owner's mobile number.")
    @Pattern(regexp = "^0?[^0\\D]\\d{8}$", message = "Mobile number must be 10 digits, starting with 0.")
    private String mobile;

    @NotBlank(message = "Please enter the owner's first name.")
    @Size(min = 3, max = 100, message = "First name must be at least 3 characters.")
    @Pattern(regexp = "^[A-Za-z'\\s]*$", message = "First name can contain only letters, spaces, and apostrophes.")
    private String firstName;

    @NotBlank(message = "Please enter the owner's last name.")
    @Size(min = 3, max = 100, message = "Last name must be at least 3 characters.")
    @Pattern(regexp = "^[A-Za-z'\\s]*$", message = "Last name can contain only letters, spaces, and apostrophes.")
    private String lastName;

    @Lob
    @NotBlank(message = "Please enter the owner's address.")
    @Size(min = 5, max = 500, message = "Address must be at least 5 characters.")
    private String address;

    @NotBlank(message = "Please enter the password.")
    @Size(min = 8, max = 20, message = "Password must be at least 8 characters.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\d\\s\\w]).{8,}$", message = "Password is not strong enough. Ex: P@ssw0rd")
    private String password;

    @NotBlank(message = "Please enter the confirm password.")
    private String confirmPassword;
}
