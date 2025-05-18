package lk.binuri.controller;

import jakarta.validation.Valid;
import lk.binuri.dto.AuthRequestDTO;
import lk.binuri.dto.AuthResponseDTO;
import lk.binuri.entity.User;
import lk.binuri.repository.UserRepository;
import lk.binuri.security.JWTService;
import lk.binuri.util.BadLoginException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody @Valid AuthRequestDTO authRequest) {
        User user = userRepository.findByUsername(authRequest.getUsername());
        final String errMsg = "Invalid username or password.";
        if (user == null) {
            throw new BadLoginException(errMsg);
        }

        if (!user.getUserType().name().equals(authRequest.getType())) {
            throw new BadLoginException(errMsg);
        }

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new BadLoginException(errMsg);
        }

        String token = jwtService.generateToken(user);
        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setUsername(user.getUsername());
        authResponseDTO.setToken(token);
        authResponseDTO.setRole(user.getUserType());
        switch (authRequest.getType()) {
            case "VEHICLE":
                authResponseDTO.setData(user.getVehicle());
                break;
            case "FUEL_STATION":
                authResponseDTO.setData(user.getFuelStation());
                break;
            case "ADMIN":
                break;
        }
        return authResponseDTO;
    }
}
