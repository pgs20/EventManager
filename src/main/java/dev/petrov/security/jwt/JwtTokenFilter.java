package dev.petrov.security.jwt;

import dev.petrov.dto.usersDto.User;
import dev.petrov.entity.UserEntity;
import dev.petrov.repository.UserRepository;
import dev.petrov.service.UsersService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenFilter.class);
    private final JwtTokenManager jwtTokenManager;
    private final UserRepository userRepository;

    public JwtTokenFilter(JwtTokenManager jwtTokenManager, UserRepository userRepository) {
        this.jwtTokenManager = jwtTokenManager;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = authorizationHeader.substring(7);
        String loginFromToken;
        try {
            loginFromToken =  jwtTokenManager.getLoginFromToken(jwtToken);
        } catch (Exception e) {
            log.error("Не удалось извлечь логин из токена");
            filterChain.doFilter(request, response);
            return;
        }

        UserEntity userEntity = userRepository.findUserByLogin(loginFromToken)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userEntity,
                null,
                List.of(new SimpleGrantedAuthority(userEntity.getRole()))
        );
        SecurityContextHolder.getContext().setAuthentication(token);
        filterChain.doFilter(request, response);
    }
}
