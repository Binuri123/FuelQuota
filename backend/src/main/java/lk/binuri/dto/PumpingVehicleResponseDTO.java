package lk.binuri.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PumpingVehicleResponseDTO {
    private String vehicleNo;
    private QuotaDetailsResponseDTO quotaDetails;
}
