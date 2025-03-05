package hu.szamla.service;

import hu.szamla.controller.dto.RoleDTO;
import hu.szamla.entity.Role;
import hu.szamla.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository repository;

    public List<RoleDTO> getAllRolesWithoutAdmin() {
        return repository.findAll().stream()
                .filter(role -> !role.getName().equals("ADMIN"))
                .map(this::convertToRoleDTO)
                .toList();
    }

    public List<RoleDTO> getAllRoles() {
        return repository.findAll().stream()
                .map(this::convertToRoleDTO)
                .toList();
    }

    private RoleDTO convertToRoleDTO(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        return roleDTO;
    }
}
