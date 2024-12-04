package dev.petrov.service;

import dev.petrov.dto.usersDto.AuthUserDto;
import dev.petrov.dto.usersDto.User;
import dev.petrov.security.jwt.JwtTokenManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenManager jwtTokenManager;

    public AuthenticationService(AuthenticationManager authenticationManager, UserService userService, JwtTokenManager jwtTokenManager) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenManager = jwtTokenManager;
    }

    public String authenticateUser(AuthUserDto authUserDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authUserDto.getLogin(),
                        authUserDto.getPassword()
                )
        );

        User user = userService.findUserByLogin(authUserDto.getLogin());

        return jwtTokenManager.generateToken(user.getLogin(), user.getRole().name());
    }
}
