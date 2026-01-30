package dev.sudhanshu.auth_n_authz.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class BcryptConfig {

    @Bean
    @Primary
    BCryptPasswordEncoder getBcryptEncoder() {
        return new BCryptPasswordEncoder();
    }
}
