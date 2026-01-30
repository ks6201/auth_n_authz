package dev.sudhanshu.auth_n_authz.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

@Configuration
public class Argon2Config {

    @Bean
    // @Primary
    Argon2PasswordEncoder getArgon2Encoder() {
        return new Argon2PasswordEncoder(
            16,
            32,
            1,
            65536,
            10
        );
    }
}
