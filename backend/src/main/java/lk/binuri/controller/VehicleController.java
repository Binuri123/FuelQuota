package lk.binuri.controller;

import com.google.zxing.WriterException;
import jakarta.validation.Valid;
import lk.binuri.dto.QuotaDetailsResponseDTO;
import lk.binuri.dto.VehicleRegisterRequestDTO;
import lk.binuri.entity.QuotaAllocation;
import lk.binuri.entity.User;
import lk.binuri.entity.Vehicle;
import lk.binuri.repository.PumpingLogRepository;
import lk.binuri.repository.QuotaAllocationRepository;
import lk.binuri.repository.UserRepository;
import lk.binuri.repository.VehicleRepository;
import lk.binuri.dto.AuthResponseDTO;
import lk.binuri.security.JWTService;
import lk.binuri.security.UserType;
import lk.binuri.service.QRCodeService;
import lk.binuri.util.CustomErrorException;
import lk.binuri.util.RmvMockApi;
import lk.binuri.util.WeekUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {
    private final VehicleRepository vehicleRepository;
    private final RmvMockApi rmvMockApi;
    private final QRCodeService qrCodeService;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final QuotaAllocationRepository quotaAllocationRepository;
    private final PumpingLogRepository pumpingLogRepository;

    public VehicleController(
            VehicleRepository vehicleRepository,
            RmvMockApi rmvMockApi,
            QRCodeService qrCodeService,
            JWTService jwtService,
            PasswordEncoder passwordEncoder,
            QuotaAllocationRepository quotaAllocationRepository,
            PumpingLogRepository pumpingLogRepository
    ) {
        this.vehicleRepository = vehicleRepository;
        this.rmvMockApi = rmvMockApi;
        this.qrCodeService = qrCodeService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.quotaAllocationRepository = quotaAllocationRepository;
        this.pumpingLogRepository = pumpingLogRepository;
    }

    @PostMapping("/register")
    public AuthResponseDTO register(@RequestBody @Valid VehicleRegisterRequestDTO vehicleRegisterRequest) throws IOException, WriterException {
        checkDuplicateValues(vehicleRegisterRequest);

        if (!rmvMockApi.verify(vehicleRegisterRequest.getVehicleNo(), vehicleRegisterRequest.getChassisNo(), vehicleRegisterRequest.getType())) {
            CustomErrorException customErrorException = new CustomErrorException();
            customErrorException.addError("vehicleNo", "This vehicle is not verified...");
            throw customErrorException;
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNo(vehicleRegisterRequest.getVehicleNo());
        vehicle.setChassisNo(vehicleRegisterRequest.getChassisNo());
        vehicle.setType(vehicleRegisterRequest.getType());
        vehicle.setFirstName(vehicleRegisterRequest.getFirstName());
        vehicle.setLastName(vehicleRegisterRequest.getLastName());
        vehicle.setFuelType(vehicleRegisterRequest.getFuelType());
        vehicle.setAddress(vehicleRegisterRequest.getAddress());
        vehicle.setOwnerNic(vehicleRegisterRequest.getOwnerNic());
        vehicle.setMobile(vehicleRegisterRequest.getMobile());

        byte[] qrCode = qrCodeService.generateQRCode(vehicle.getVehicleNo());
        vehicle.setQr(qrCode);

        User user = new User();
        user.setUsername(vehicle.getVehicleNo());
        user.setPassword(passwordEncoder.encode(vehicleRegisterRequest.getPassword()));
        user.setUserType(UserType.VEHICLE);
        vehicle.setUser(user);
        vehicleRepository.save(vehicle);

        String token = jwtService.generateToken(user);
        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setUsername(vehicle.getVehicleNo());
        authResponseDTO.setToken(token);
        authResponseDTO.setRole(user.getUserType());
        authResponseDTO.setData(vehicle);
        return authResponseDTO;
    }

    private void checkDuplicateValues(VehicleRegisterRequestDTO vehicleRegisterRequest) {
        Vehicle vehicleByVehicleNo = vehicleRepository.findByVehicleNo(vehicleRegisterRequest.getVehicleNo());
        Vehicle vehicleByChassisNo = vehicleRepository.findByChassisNo(vehicleRegisterRequest.getChassisNo());
        Vehicle vehicleByNic = vehicleRepository.findByOwnerNic(vehicleRegisterRequest.getOwnerNic());
        Vehicle vehicleByMobileNo = vehicleRepository.findByMobile(vehicleRegisterRequest.getMobile());

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

    @GetMapping("/qr")
    public ResponseEntity<byte[]> getQR(Principal principal) {
        String vehicleNo = principal.getName();
        Vehicle vehicle = vehicleRepository.findByVehicleNo(vehicleNo);
        if (vehicle == null) {
            return ResponseEntity.notFound().build();
        }
        byte[] qrImage = vehicle.getQr();
        if (qrImage != null && qrImage.length > 0) {
            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
                    .body(qrImage);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/quota-details")
    public QuotaDetailsResponseDTO getQuotaDetails(Principal principal) {
        String vehicleNo = principal.getName();
        LocalDateTime startDate = WeekUtil.getStartOfWeek();
        LocalDateTime endDate = WeekUtil.getEndOfWeek();

        Vehicle vehicle = vehicleRepository.findByVehicleNo(vehicleNo);
        QuotaAllocation quotaAllocation = quotaAllocationRepository.getReferenceById(vehicle.getType());
        Optional<Double> pumpedAmount = pumpingLogRepository.getTotalFuelPumped(vehicleNo, startDate, endDate);

        QuotaDetailsResponseDTO response = new QuotaDetailsResponseDTO();
        response.setQuota(quotaAllocation.getAmount());
        response.setBalance(quotaAllocation.getAmount() - pumpedAmount.orElse(0.0));
        return response;
    }
}
