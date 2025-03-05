package hu.szamla.controller;

import hu.szamla.controller.dto.HomeDTO;
import hu.szamla.entity.User;
import hu.szamla.service.HomeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/home")
@AllArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @GetMapping
    public ResponseEntity<HomeDTO> getHomeData(@AuthenticationPrincipal User user) {
        HomeDTO homeDTO = homeService.getHomeData(user);
        return ResponseEntity.ok(homeDTO);
    }
}
