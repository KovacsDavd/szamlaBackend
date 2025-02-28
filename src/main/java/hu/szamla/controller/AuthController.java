package hu.szamla.controller;

import hu.szamla.controller.dto.RegDTO;
import hu.szamla.controller.dto.UserDTO;
import hu.szamla.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin("*")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/auth/signup")
    public ResponseEntity<UserDTO> signUp(@RequestBody RegDTO regDTO) {
        return ResponseEntity.ok(authService.signUp(regDTO));
    }
}
