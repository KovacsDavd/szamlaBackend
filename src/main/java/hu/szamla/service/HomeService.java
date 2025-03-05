package hu.szamla.service;

import hu.szamla.controller.dto.HomeDTO;
import hu.szamla.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HomeService {
    private static final String INVOICES = "INVOICES";
    private static final String CREATE_INVOICE = "CREATE_INVOICE";

    public HomeDTO getHomeData(User user) {
        Set<String> menuItems = new HashSet<>();
        user.getAuthorities().forEach(authority -> {
            String role = authority.getAuthority();
            switch (role) {
                case "ADMIN":
                    menuItems.add("ADMIN");
                    menuItems.add(CREATE_INVOICE);
                    break;
                case "ACCOUNTANT":
                    menuItems.add(CREATE_INVOICE);
                    break;
                default:
                    menuItems.add(null);
                    break;
            }
            menuItems.add(INVOICES);
        });

        Set<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        HomeDTO homeDTO = new HomeDTO();
        homeDTO.setUsername(user.getUsername());
        homeDTO.setMenuItems(menuItems);
        homeDTO.setRoles(roles);
        homeDTO.setLastLoginDate(user.getLastLoginDate());
        return homeDTO;
    }
}
