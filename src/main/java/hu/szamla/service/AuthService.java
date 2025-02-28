package hu.szamla.service;

import hu.szamla.controller.dto.*;
import hu.szamla.entity.Role;
import hu.szamla.entity.User;
import hu.szamla.entity.UserRole;
import hu.szamla.repository.RoleRepository;
import hu.szamla.repository.UserRepository;
import hu.szamla.utils.JwtUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

        User user = dtoToEntity(regDTO, encodedPassword, role);

        User savedUser = userRepository.save(user);

        return convertToUserDTO(savedUser);
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

            Set<RoleDTO> roles = convertRolesToDTOs(user.getUserRoles());

            String jwt = jwtUtils.generateToken(user);
            LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
            loginResponseDTO.setUsername(user.getUsername());
            loginResponseDTO.setToken(jwt);
            loginResponseDTO.setRoles(roles);

            return loginResponseDTO;
        } catch (Exception e) {
            throw new EntityNotFoundException("Wrong username or password");
        }
    }

    private User dtoToEntity(RegDTO regDTO, String encodedPassword, Role role) {
        User user = new User();
        user.setUsername(regDTO.getUsername());
        user.setPassword(encodedPassword);
        user.setName(regDTO.getName());

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);

        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(userRole);
        user.setUserRoles(userRoles);

        return user;
    }

    private UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setName(user.getName());
        userDTO.setLastLoginDate(user.getLastLoginDate());

        Set<RoleDTO> roles = convertRolesToDTOs(user.getUserRoles());

        userDTO.setRoles(roles);

        return userDTO;
    }

    private Set<RoleDTO> convertRolesToDTOs(Set<UserRole> userRoles) {
        return userRoles.stream()
                .map(userRole -> {
                    RoleDTO roleDTO = new RoleDTO();
                    roleDTO.setId(userRole.getRole().getId());
                    roleDTO.setName(userRole.getRole().getName());
                    roleDTO.setDescription(userRole.getRole().getDescription());
                    return roleDTO;
                })
                .collect(Collectors.toSet());
    }
}
