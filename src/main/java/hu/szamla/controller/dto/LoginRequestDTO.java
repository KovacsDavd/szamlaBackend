package hu.szamla.controller.dto;

import lombok.Data;

import java.util.Arrays;

@Data
public class LoginRequestDTO {
    private String username;
    private char[] password;

    public void clearPassword() {
        if (password != null) {
            Arrays.fill(password, '\0');
        }
    }
}
