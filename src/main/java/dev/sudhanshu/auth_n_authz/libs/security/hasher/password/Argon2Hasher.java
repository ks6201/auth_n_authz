package dev.sudhanshu.auth_n_authz.libs.security.hasher.password;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
// @Primary
public class Argon2Hasher implements PasswordHasher {

    @Autowired
    private Argon2PasswordEncoder argon2Encoder;

    @Override
    public String hash(String rawPassword) {
        return this.argon2Encoder.encode(rawPassword);
    }

    @Override
    public boolean verify(String rawPassword, String hashedPassword) {
        return this.argon2Encoder.matches(rawPassword, hashedPassword);
    }
}
