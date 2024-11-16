package dev.petrov.dto.usersDto;

import jakarta.validation.constraints.NotBlank;

public class AuthUserDto {
    @NotBlank
    private String login;
    @NotBlank
    private String password;

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
