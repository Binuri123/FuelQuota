package lk.binuri.controller;

import lk.binuri.entity.Vehicle;
import lk.binuri.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VehicleController {
    @Autowired
    VehicleRepository vehicleRepository;

    @PostMapping("/register")
    public Vehicle register(@RequestBody Vehicle vehicle){
        //To Do: Basic Validations
        //Validate with rmv System
        vehicleRepository.save(vehicle);
        return vehicle;
    }
}
