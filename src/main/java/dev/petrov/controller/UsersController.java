package dev.petrov.controller;

import dev.petrov.converter.ConverterUser;
import dev.petrov.dto.JwtResponseDto;
import dev.petrov.dto.usersDto.AuthUserDto;
import dev.petrov.dto.usersDto.UserDto;
import dev.petrov.dto.usersDto.UserRegistrationDto;
import dev.petrov.service.AuthenticationService;
import dev.petrov.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {

    private static final Logger log = LoggerFactory.getLogger(UsersController.class);
    private final ConverterUser converterUser;
    private final UserService userService;
    private final AuthenticationService jwtAuthenticationService;

    public UsersController(ConverterUser converter, UserService usersService, AuthenticationService jwtAuthenticationService) {
        this.converterUser = converter;
        this.userService = usersService;
        this.jwtAuthenticationService = jwtAuthenticationService;
    }

    @PostMapping
    public ResponseEntity<UserDto> userRegistration(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        log.info("Регистрация пользователя {}", userRegistrationDto.getLogin());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                converterUser.toDto(
                        userService.registrationUser(
                            converterUser.toDomain(
                                    userRegistrationDto)
                        )
                )
        );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getInfoUserById(@PathVariable Integer userId) {
        log.info("Получение информации о пользователе c id={}", userId);

        return ResponseEntity.ok().body(
                converterUser.toDto(
                        userService.getInfoUserById(userId)
                )
        );
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtResponseDto> authUser(@Valid @RequestBody AuthUserDto authUserDto) {
        log.info("Аутентификация пользователя {}", authUserDto.getLogin());

        return ResponseEntity.ok().body(
                new JwtResponseDto(
                        jwtAuthenticationService.authenticateUser(authUserDto)
                )
        );
    }
}
