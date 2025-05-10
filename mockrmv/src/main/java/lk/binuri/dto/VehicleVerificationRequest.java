package lk.binuri.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleVerificationRequest {
    private String vehicleNo;
    private String chassisNo;
    private String type;
}
