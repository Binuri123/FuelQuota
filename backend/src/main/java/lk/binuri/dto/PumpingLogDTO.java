package lk.binuri.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PumpingLogDTO {
    @NotBlank(message = "Please enter the vehicle number.")
    @Pattern(regexp = "^[A-Za-z0-9]{1,4}-\\d{4}$", message = "Vehicle number must follow the format: ABC-1234.")
    private String vehicleNo;

    @DecimalMin(value = "0.1",message = "Minimum value should be 0.1")
    @NotNull(message = "Amount should be enter")
    private Double amount;
}
