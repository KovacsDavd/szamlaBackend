package hu.szamla.controller.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class LoginResponseDTO {
    private String token;
    private String username;
    private LocalDateTime lastLoginDate;
    private Set<RoleDTO> roles;
}
