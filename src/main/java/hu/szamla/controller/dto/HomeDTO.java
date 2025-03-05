package hu.szamla.controller.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class HomeDTO {
    private String username;
    private Set<String> roles;
    private LocalDateTime lastLoginDate;
    private Set<String> menuItems;
}
