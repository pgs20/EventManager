package dev.petrov.controller;

import dev.petrov.converter.Converter;
import dev.petrov.dto.JwtResponseDto;
import dev.petrov.dto.usersDto.AuthUserDto;
import dev.petrov.dto.usersDto.UserDto;
import dev.petrov.dto.usersDto.UserRegistrationDto;
import dev.petrov.service.JwtAuthenticationService;
import dev.petrov.service.UsersService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final Converter converter;
    private final UsersService usersService;
    private final JwtAuthenticationService jwtAuthenticationService;

    public UsersController(Converter converter, UsersService usersService, JwtAuthenticationService jwtAuthenticationService) {
        this.converter = converter;
        this.usersService = usersService;
        this.jwtAuthenticationService = jwtAuthenticationService;
    }

    @PostMapping
    public ResponseEntity<UserDto> userRegistration(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                converter.toDto(
                        usersService.registrationUser(
                            converter.toDomain(
                                    userRegistrationDto)
                        )
                )
        );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getInfoUserById(@PathVariable Integer userId) {
        return ResponseEntity.ok().body(
                converter.toDto(
                        usersService.getInfoUserById(userId)
                )
        );
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtResponseDto> authUser(@Valid @RequestBody AuthUserDto authUserDto) {
        return ResponseEntity.ok().body(
                new JwtResponseDto(
                        jwtAuthenticationService.authenticateUser(authUserDto)
                )
        );
    }
}
