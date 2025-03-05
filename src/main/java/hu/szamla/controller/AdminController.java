package hu.szamla.controller;

import hu.szamla.controller.dto.UserDTO;
import hu.szamla.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController()
@RequestMapping("/api/admin")
@AllArgsConstructor
public class AdminController {
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable final Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PutMapping("/user/roles/{userId}")
    public ResponseEntity<UserDTO> updateUserRoles(@PathVariable Long userId, @RequestBody Set<String> roles) {
        return ResponseEntity.ok(userService.updateUserRoles(userId, roles));
    }
}
