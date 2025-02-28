package hu.szamla.controller.dto;

import lombok.Data;

import java.util.Set;

@Data
public class LoginResponseDTO {
    private String token;
    private String username;
    private Set<RoleDTO> roles;
}
