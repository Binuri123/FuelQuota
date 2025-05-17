package lk.binuri.controller;

import com.google.zxing.WriterException;
import jakarta.validation.Valid;
import lk.binuri.dto.PumpingLogDTO;
import lk.binuri.dto.PumpingLogResponseDTO;
import lk.binuri.entity.FuelStation;
import lk.binuri.entity.PumpingLog;
import lk.binuri.entity.QuotaAllocation;
import lk.binuri.entity.Vehicle;
import lk.binuri.repository.FuelStationRepository;
import lk.binuri.repository.PumpingLogRepository;
import lk.binuri.repository.QuotaAllocationRepository;
import lk.binuri.repository.VehicleRepository;
import lk.binuri.util.WeekUtil;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api/pumping")
public class PumpingController {
    private final PumpingLogRepository pumpingLogRepository;
    private final QuotaAllocationRepository quotaAllocationRepository;
    private final VehicleRepository vehicleRepository;
    private final FuelStationRepository fuelStationRepository;

    public PumpingController(
            PumpingLogRepository pumpingLogRepository,
            QuotaAllocationRepository quotaAllocationRepository,
            VehicleRepository vehicleRepository,
            FuelStationRepository fuelStationRepository
    ){
        this.pumpingLogRepository=pumpingLogRepository;
        this.quotaAllocationRepository=quotaAllocationRepository;
        this.vehicleRepository=vehicleRepository;
        this.fuelStationRepository = fuelStationRepository;
    }

    @PostMapping
    public PumpingLogResponseDTO insert(@RequestBody @Valid PumpingLogDTO pumpingLogDTO, Principal principal) throws IOException, WriterException {
        String fuelStationCode = principal.getName();
        LocalDateTime startAt= WeekUtil.getStartOfWeek();
        LocalDateTime endAt=WeekUtil.getEndOfWeek();
        Vehicle vehicle = vehicleRepository.findByVehicleNo(pumpingLogDTO.getVehicleNo());
        Optional<Double> optionalPumpedAmount=pumpingLogRepository.getTotalFuelPumped(pumpingLogDTO.getVehicleNo(),startAt,endAt);
        QuotaAllocation quotaAllocation = quotaAllocationRepository.getReferenceById(vehicle.getType());
        FuelStation fuelStation = fuelStationRepository.findByCode(fuelStationCode);

        PumpingLog pumpingLog = new PumpingLog();
        pumpingLog.setId(UUID.randomUUID());
        pumpingLog.setVehicle(vehicle);
        pumpingLog.setFuelStation(fuelStation);
        pumpingLog.setPumpedAt(LocalDateTime.now());
        pumpingLog.setAmount(pumpingLogDTO.getAmount());

        pumpingLogRepository.save(pumpingLog);

        PumpingLogResponseDTO response = new PumpingLogResponseDTO();
        response.setSuccess(true);
        return response;
    }
}
