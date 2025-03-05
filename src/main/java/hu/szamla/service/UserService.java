package hu.szamla.service;

import hu.szamla.controller.dto.UserDTO;
import hu.szamla.entity.Role;
import hu.szamla.entity.User;
import hu.szamla.entity.UserRole;
import hu.szamla.repository.RoleRepository;
import hu.szamla.repository.UserRepository;
import hu.szamla.repository.UserRoleRepository;
import hu.szamla.utils.MapperUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException("User " + username + " not found"));
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(MapperUtils::convertToUserDTO)
                .toList();
    }

    public void deleteUser(Long userId) {
        userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );
        userRepository.deleteById(userId);
    }

    @Transactional
    public UserDTO updateUserRoles(Long userId, Set<String> roles) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Set<Role> newRoles = roles.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName)))
                .collect(Collectors.toSet());

        Set<UserRole> newUserRoles = new HashSet<>();
        for (Role role : newRoles) {
            Optional<UserRole> existingUserRoleOpt = user.getUserRoles().stream()
                    .filter(ur -> ur.getRole().getId().equals(role.getId()))
                    .findFirst();
            if (existingUserRoleOpt.isPresent()) {
                newUserRoles.add(existingUserRoleOpt.get());
            } else {
                UserRole userRole = new UserRole();
                userRole.setUser(user);
                userRole.setRole(role);
                newUserRoles.add(userRole);
            }
        }

        user.getUserRoles().clear();
        user.getUserRoles().addAll(newUserRoles);

        User updatedUser = userRepository.save(user);

        return MapperUtils.convertToUserDTO(updatedUser);
    }

}
