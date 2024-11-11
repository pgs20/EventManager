package dev.petrov.service;

import dev.petrov.dto.usersDto.AuthUserDto;
import dev.petrov.security.jwt.JwtTokenManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class JwtAuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenManager jwtTokenManager;

    public JwtAuthenticationService(AuthenticationManager authenticationManager, JwtTokenManager jwtTokenManager) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenManager = jwtTokenManager;
    }

    public String authenticateUser(AuthUserDto authUserDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authUserDto.getLogin(),
                        authUserDto.getPassword()
                )
        );
        return jwtTokenManager.generateToken(authUserDto.getLogin());
    }
}
