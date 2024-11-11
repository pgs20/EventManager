package dev.petrov.dto;

public class JwtResponseDto {
    private final String jwtToken;

    public JwtResponseDto(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }
}
