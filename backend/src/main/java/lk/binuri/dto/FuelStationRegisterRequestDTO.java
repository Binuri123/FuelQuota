package lk.binuri.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FuelStationRegisterRequestDTO {
    @NotBlank(message = "Please enter the station code.")
    @Size(min = 4, max = 25, message = "Code must be between 4 and 25 characters.")
    private String code;

    @NotBlank(message = "Please enter the station name.")
    @Size(min = 3, max = 100, message = "Station name must be at least 3 characters.")
    private String name;

    @NotBlank(message = "Please enter the dealer's name.")
    @Size(min = 3, max = 100, message = "Dealer's name must be at least 3 characters.")
    private String dealer;

    @NotBlank(message = "Please enter the station city.")
    @Size(min = 3, max = 50, message = "City name must be at least 3 characters.")
    private String city;

    @NotBlank(message = "Please enter a contact number.")
    @Pattern(regexp = "^0?[^0\\D]\\d{8}$", message = "Contact number must be 10 digits, starting with 0.")
    private String contact1;

    @Pattern(regexp = "^(0?[^0\\D]\\d{8})?$", message = "Contact number must be 10 digits, starting with 0.")
    private String contact2;

    @NotBlank(message = "Please enter the company.")
    @Pattern(regexp = "^(Ceypetco|IOC|Sinopec|Shell|United Petroleum)$", message = "Vehicle type must be one of: Ceypetco,IOC,Sinopec, Shell, or United Petroleum.")
    private String company;

    @Lob
    @NotBlank(message = "Please enter the fuel station's address.")
    @Size(min = 5, max = 500, message = "Address must be at least 5 characters.")
    private String address;

    @NotBlank(message = "Please enter the password.")
    @Size(min = 8, max = 20, message = "Password must be at least 8 characters.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\d\\s\\w]).{8,}$", message = "Password is not strong enough. Ex: P@ssw0rd")
    private String password;

    @NotBlank(message = "Please enter the confirm password.")
    private String confirmPassword;
}
