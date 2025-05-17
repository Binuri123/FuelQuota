package lk.binuri.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequestDTO {
    @NotBlank(message = "Username is required.")
    private String username;

    @NotBlank(message = "Password is required.")
    private String password;

    @NotBlank(message = "Type is required.")
    @Pattern(regexp = "^(VEHICLE|ADMIN|FUEL_STATION)$", message = "Type must be either VEHICLE,ADMIN or FUEL_STATION.")
    private String type;
}
