package dev.petrov.dto.usersDto;

import dev.petrov.validator.UniqueLogin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserRegistrationDto {

    @NotBlank
    private String login;
    @NotBlank
    private String password;
    @NotNull
    @Min(value = 18)
    private Integer age;

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Integer getAge() {
        return age;
    }
}
