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
import lk.binuri.service.SMSService;
import lk.binuri.util.CustomErrorException;
import lk.binuri.util.WeekUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pumping")
public class PumpingController {
    private final PumpingLogRepository pumpingLogRepository;
    private final QuotaAllocationRepository quotaAllocationRepository;
    private final VehicleRepository vehicleRepository;
    private final FuelStationRepository fuelStationRepository;
    private final SMSService smsService;

    @PostMapping
    public PumpingLogResponseDTO insert(@RequestBody @Valid PumpingLogDTO pumpingLogDTO, Principal principal) throws IOException, WriterException {
        CustomErrorException customErrorException = new CustomErrorException();
        Vehicle vehicle = vehicleRepository.findByVehicleNo(pumpingLogDTO.getVehicleNo());

        if (vehicle == null) {
            customErrorException.addError("vehicleNo", "Vehicle number is not registered yet");
            throw customErrorException;
        }

        QuotaAllocation quotaAllocation = quotaAllocationRepository.findById(vehicle.getType()).orElse(null);
        if (quotaAllocation == null) {
            customErrorException.addError("vehicleNo", "Unable to find quota allocation for vehicle type: " + vehicle.getType());
            throw customErrorException;
        }

        String fuelStationCode = principal.getName();
        LocalDateTime startAt = WeekUtil.getStartOfWeek();
        LocalDateTime endAt = WeekUtil.getEndOfWeek();

        Optional<Double> optionalPumpedAmount = pumpingLogRepository.getTotalFuelPumped(pumpingLogDTO.getVehicleNo(), startAt, endAt);
        Double pumpedAmount = optionalPumpedAmount.orElse(0.0);
        Integer quotaAmount = quotaAllocation.getAmount();

        double remainingAfterPump = quotaAmount - (pumpedAmount + pumpingLogDTO.getAmount());

        if (remainingAfterPump < 0) {
            customErrorException.addError("amount", "Insufficient fuel quota balance");
            throw customErrorException;
        }

        FuelStation fuelStation = fuelStationRepository.findByCode(fuelStationCode);
        PumpingLog pumpingLog = new PumpingLog();
        pumpingLog.setId(UUID.randomUUID());
        pumpingLog.setVehicle(vehicle);
        pumpingLog.setFuelStation(fuelStation);
        pumpingLog.setPumpedAt(LocalDateTime.now());
        pumpingLog.setAmount(pumpingLogDTO.getAmount());
        pumpingLogRepository.save(pumpingLog);

        String smsTemplate = "National Fuel Pass: TRN confirmed.\n%s\n%s\nQuota used: %.2fL\nWeekly Balance: %.2fL\nStation Code: %s\n(Expires on - %s)";
        String dateTimeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        String vehicleNo = vehicle.getVehicleNo();
        double quotaUsed = pumpingLogDTO.getAmount();
        String stationCode = fuelStation.getCode();
        String expireDateTimeStr = endAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        String message = String.format(smsTemplate, dateTimeStr, vehicleNo, quotaUsed, remainingAfterPump, stationCode, expireDateTimeStr);
        smsService.sendSms(vehicle.getMobile(), message);

        PumpingLogResponseDTO response = new PumpingLogResponseDTO();
        response.setSuccess(true);
        return response;
    }
}
