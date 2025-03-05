package hu.szamla.controller;

import hu.szamla.controller.dto.LoginRequestDTO;
import hu.szamla.controller.dto.LoginResponseDTO;
import hu.szamla.controller.dto.RegDTO;
import hu.szamla.controller.dto.UserDTO;
import hu.szamla.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signUp(@RequestBody RegDTO regDTO) {
        return ResponseEntity.ok(authService.signUp(regDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(authService.login(loginRequestDTO));
    }

    @GetMapping("/validateToken")
    public ResponseEntity<Boolean> validateToken() {
        return ResponseEntity.ok(true);
    }
}
