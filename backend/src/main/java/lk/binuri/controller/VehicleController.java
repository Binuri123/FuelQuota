package lk.binuri.controller;

import com.google.zxing.WriterException;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lk.binuri.entity.Vehicle;
import lk.binuri.repository.VehicleRepository;
import lk.binuri.service.QRCodeService;
import lk.binuri.util.CustomErrorException;
import lk.binuri.util.RmvMockApi;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@CrossOrigin
@RestController
public class VehicleController {
    VehicleRepository vehicleRepository;
    RmvMockApi rmvMockApi;
    QRCodeService qrCodeService;

    public VehicleController(VehicleRepository vehicleRepository, RmvMockApi rmvMockApi, QRCodeService qrCodeService) {
        this.vehicleRepository = vehicleRepository;
        this.rmvMockApi = rmvMockApi;
        this.qrCodeService = qrCodeService;
    }

    @PostMapping("/register")
    public Vehicle register(@RequestBody @Valid Vehicle vehicle) throws IOException, WriterException {
        checkDuplicateValues(vehicle);
        if (!rmvMockApi.verify(vehicle.getVehicleNo(), vehicle.getChassisNo(), vehicle.getType())) {
            CustomErrorException customErrorException = new CustomErrorException();
            customErrorException.addError("vehicleNo", "This vehicle is not verified...");
            throw customErrorException;
        }

        String qrText = vehicle.getVehicleNo() + "|" + UUID.randomUUID();
        byte[] qrCode = qrCodeService.generateQRCode(qrText, 200, 200);
        vehicle.setQr(qrCode);

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

    @GetMapping("/qr/{vehicleNo}")
    public ResponseEntity<byte[]> getQR(@PathVariable String vehicleNo) {
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
}
