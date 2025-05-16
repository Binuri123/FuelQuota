package lk.binuri.repository;

import lk.binuri.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
    // You can define custom query methods here if needed, for example:
    Vehicle findByVehicleNo(String vehicleNo);
    Vehicle findByChassisNo(String chassisNo);
    Vehicle findByOwnerNic(String nic);
    Vehicle findByMobile(String mobile);
}