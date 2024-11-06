package dev.petrov.controller;

import dev.petrov.converter.Converter;
import dev.petrov.dto.usersDto.UserDto;
import dev.petrov.dto.usersDto.UserRegistrationDto;
import dev.petrov.service.UsersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final Converter converter;
    private final UsersService usersService;

    public UsersController(Converter converter, UsersService usersService) {
        this.converter = converter;
        this.usersService = usersService;
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
}
