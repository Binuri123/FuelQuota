package lk.binuri.controller;

import lk.binuri.dto.VehicleVerificationRequest;
import lk.binuri.dto.VehicleVerificationResponse;
import lk.binuri.entity.Vehicle;
import lk.binuri.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VerificationController {

    @Autowired
    VehicleRepository vehicleRepository;

    @PostMapping("/verify-vehicle-details")
    public VehicleVerificationResponse verify(@RequestBody VehicleVerificationRequest request) {
        String vehicleNo = request.getVehicleNo();
        String chassisNo = request.getChassisNo();
        String type = request.getType();

        Vehicle vehicle = vehicleRepository.findByVehicleNo(vehicleNo);

        VehicleVerificationResponse response = new VehicleVerificationResponse();

        response.setSuccess(vehicle != null && vehicle.getChassisNo().equalsIgnoreCase(chassisNo) && vehicle.getType().equalsIgnoreCase(type));
        return response;
    }
}
