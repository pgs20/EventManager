package dev.petrov.config;

import dev.petrov.security.CustomAccessDeniedHandler;
import dev.petrov.security.CustomAuthenticationEntryPoint;
import dev.petrov.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    public SecurityConfiguration(CustomUserDetailsService customUserDetailsService,
                                 CustomAuthenticationEntryPoint authenticationEntryPoint,
                                 CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.customUserDetailsService = customUserDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers(HttpMethod.GET, "/locations").hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers(HttpMethod.POST, "/locations").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/locations/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/locations/**").hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers(HttpMethod.PUT, "/locations/**").hasAuthority("ADMIN")

                                .requestMatchers(HttpMethod.GET, "/users/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/users", "/users/auth").permitAll()
                                .anyRequest().authenticated()
                ).exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler))
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService(customUserDetailsService);
        authenticationProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());

        return authenticationProvider;
    }
}
