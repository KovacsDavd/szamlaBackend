package hu.szamla.utils;

import hu.szamla.controller.dto.InvoiceDTO;
import hu.szamla.controller.dto.RegDTO;
import hu.szamla.controller.dto.RoleDTO;
import hu.szamla.controller.dto.UserDTO;
import hu.szamla.entity.Invoice;
import hu.szamla.entity.Role;
import hu.szamla.entity.User;
import hu.szamla.entity.UserRole;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MapperUtils {

    private MapperUtils() {
    }

    public static UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setName(user.getName());
        userDTO.setLastLoginDate(user.getLastLoginDate());
        userDTO.setRoles(convertRolesToDTOs(user.getUserRoles()));
        return userDTO;
    }

    public static Set<RoleDTO> convertRolesToDTOs(Set<UserRole> userRoles) {
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

    public static InvoiceDTO convertToInvoiceDTO(Invoice invoice) {
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setId(invoice.getId());
        invoiceDTO.setCustomerName(invoice.getCustomerName());
        invoiceDTO.setIssueDate(invoice.getIssueDate());
        invoiceDTO.setDeadlineDate(invoice.getDeadlineDate());
        invoiceDTO.setItemName(invoice.getItemName());
        invoiceDTO.setComment(invoice.getComment());
        invoiceDTO.setPrice(invoice.getPrice());
        return invoiceDTO;
    }

    public static User convertToUser(RegDTO regDTO, String encodedPassword, Role role) {
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
}
