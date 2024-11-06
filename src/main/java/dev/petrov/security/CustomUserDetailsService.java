package dev.petrov.security;

import dev.petrov.entity.UserEntity;
import dev.petrov.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с таким логином не найден"));

        return User.withUsername(userEntity.getLogin())
                .password(userEntity.getPasswordHash())
                .authorities(userEntity.getRole())
                .build();
    }
}
