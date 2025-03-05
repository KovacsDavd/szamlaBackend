package hu.szamla.service;

import hu.szamla.controller.dto.*;
import hu.szamla.entity.Role;
import hu.szamla.entity.User;
import hu.szamla.repository.RoleRepository;
import hu.szamla.repository.UserRepository;
import hu.szamla.utils.JwtUtils;
import hu.szamla.utils.MapperUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    public UserDTO signUp(final RegDTO regDTO) {
        if (userRepository.findByUsername(regDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (regDTO.getPassword() == null || regDTO.getPassword().length == 0) {
            throw new IllegalArgumentException("Password is required");
        }

        if (regDTO.getRole() == null || regDTO.getRole().isEmpty()) {
            throw new IllegalArgumentException("Role is required");
        }

        Role role = roleRepository.findByName(regDTO.getRole())
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + regDTO.getRole()));

        String encodedPassword = passwordEncoder.encode(new String(regDTO.getPassword()));
        regDTO.clearPassword();

        User user = MapperUtils.convertToUser(regDTO, encodedPassword, role);

        User savedUser = userRepository.save(user);

        return MapperUtils.convertToUserDTO(savedUser);
    }


    public LoginResponseDTO login(final LoginRequestDTO loginRequestDTO) {
        if (loginRequestDTO.getUsername() == null || loginRequestDTO.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (loginRequestDTO.getPassword() == null || loginRequestDTO.getPassword().length == 0) {
            throw new IllegalArgumentException("Password is required");
        }

        User user = userRepository.findByUsername(loginRequestDTO.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequestDTO.getUsername(),
                    new String(loginRequestDTO.getPassword())
            ));
            loginRequestDTO.clearPassword();

            user.setLastLoginDate(LocalDateTime.now());
            userRepository.save(user);

            Set<RoleDTO> roles = MapperUtils.convertRolesToDTOs(user.getUserRoles());

            String jwt = jwtUtils.generateToken(user);
            LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
            loginResponseDTO.setUsername(user.getUsername());
            loginResponseDTO.setToken(jwt);
            loginResponseDTO.setRoles(roles);
            loginResponseDTO.setLastLoginDate(user.getLastLoginDate());

            return loginResponseDTO;
        } catch (Exception e) {
            throw new EntityNotFoundException("Wrong username or password");
        }
    }
}
