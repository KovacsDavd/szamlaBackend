package hu.szamla.controller.dto;

import lombok.Data;

import java.util.Arrays;

@Data
public class RegDTO {
    private String name;
    private String username;
    private char[] password;
    private String role;

    public void clearPassword() {
        if (password != null) {
            Arrays.fill(password, '\0');
        }
    }
}
