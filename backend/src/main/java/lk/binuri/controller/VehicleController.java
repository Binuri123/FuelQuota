package lk.binuri.controller;

import com.google.zxing.WriterException;
import jakarta.validation.Valid;
import lk.binuri.entity.User;
import lk.binuri.entity.Vehicle;
import lk.binuri.repository.UserRepository;
import lk.binuri.repository.VehicleRepository;
import lk.binuri.security.AuthResponse;
import lk.binuri.security.JWTService;
import lk.binuri.security.UserType;
import lk.binuri.service.QRCodeService;
import lk.binuri.util.CustomErrorException;
import lk.binuri.util.RmvMockApi;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.UUID;

@CrossOrigin
@RestController
public class VehicleController {
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final RmvMockApi rmvMockApi;
    private final QRCodeService qrCodeService;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public VehicleController(
            VehicleRepository vehicleRepository,
            UserRepository userRepository,
            RmvMockApi rmvMockApi,
            QRCodeService qrCodeService,
            AuthenticationManager authenticationManager,
            JWTService jwtService
    ) {
        this.vehicleRepository = vehicleRepository;
        this.rmvMockApi = rmvMockApi;
        this.qrCodeService = qrCodeService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody @Valid Vehicle vehicle) throws IOException, WriterException {
        checkDuplicateValues(vehicle);
        if (!rmvMockApi.verify(vehicle.getVehicleNo(), vehicle.getChassisNo(), vehicle.getType())) {
            CustomErrorException customErrorException = new CustomErrorException();
            customErrorException.addError("vehicleNo", "This vehicle is not verified...");
            throw customErrorException;
        }

        String qrText = vehicle.getVehicleNo() + "|" + UUID.randomUUID();
        byte[] qrCode = qrCodeService.generateQRCode(qrText, 200, 200);
        vehicle.setQr(qrCode);

        User user = new User();
        user.setUsername(vehicle.getVehicleNo());
        user.setUserType(UserType.VEHICLE);
        vehicle.setUser(user);
        vehicleRepository.save(vehicle);

        String token = jwtService.generateToken(user);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setUsername(vehicle.getVehicleNo());
        authResponse.setToken(token);
        authResponse.setRole(user.getUserType());
        authResponse.setData(vehicle);
        return authResponse;
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

    @GetMapping("/test")
    public String test(){
        return "Hello World";
    }
}
