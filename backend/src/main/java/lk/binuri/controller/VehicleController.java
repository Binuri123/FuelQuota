package lk.binuri.controller;

import jakarta.validation.Valid;
import lk.binuri.entity.Vehicle;
import lk.binuri.repository.VehicleRepository;
import lk.binuri.util.CustomErrorException;
import lk.binuri.util.RmvMockApi;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VehicleController {
    VehicleRepository vehicleRepository;
    RmvMockApi rmvMockApi;

    public VehicleController(VehicleRepository vehicleRepository, RmvMockApi rmvMockApi) {
        this.vehicleRepository = vehicleRepository;
        this.rmvMockApi = rmvMockApi;
    }

    @PostMapping("/register")
    public Vehicle register(@RequestBody @Valid Vehicle vehicle) {
        checkDuplicateValues(vehicle);
        if(!rmvMockApi.verify(vehicle.getVehicleNo(),vehicle.getChassisNo(),vehicle.getType())){
            CustomErrorException customErrorException = new CustomErrorException();
            customErrorException.addError("vehicleNo","This vehicle is not verified...");
            throw customErrorException;
        }
        
        vehicleRepository.save(vehicle);
        return vehicle;
    }

    private void checkDuplicateValues(Vehicle vehicle) {
        Vehicle vehicleByVehicleNo = vehicleRepository.findByVehicleNo(vehicle.getVehicleNo());
        Vehicle vehicleByChassisNo = vehicleRepository.findByChassisNo(vehicle.getChassisNo());
        Vehicle vehicleByNic = vehicleRepository.findByOwnerNic(vehicle.getOwnerNic());
        Vehicle vehicleByMobileNo = vehicleRepository.findByMobile(vehicle.getMobile());

        CustomErrorException customErrorException = new CustomErrorException();

        if (vehicleByVehicleNo != null) {
            customErrorException.addError("vehicleNo", "This Vehicle No is already exists...");
        }

        if (vehicleByChassisNo != null) {
            customErrorException.addError("chassisNo", "This Chassis No is already exists...");
        }

        if (vehicleByNic != null) {
            customErrorException.addError("ownerNic", "This NIC is already exists...");
        }

        if (vehicleByMobileNo != null) {
            customErrorException.addError("mobile", "This Mobile No is already exists...");
        }

        if (!customErrorException.isEmpty()) {
            throw customErrorException;
        }
    }
}
