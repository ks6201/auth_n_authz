package dev.sudhanshu.auth_n_authz.libs.security.hasher.password;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Primary
public class BCryptHasher implements PasswordHasher {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public String hash(String rawPassword) {
        return this.bCryptPasswordEncoder.encode(rawPassword);
    }

    @Override
    public boolean verify(String rawPassword, String hashedPassword) {
        return this.bCryptPasswordEncoder.matches(rawPassword, hashedPassword);
    }

}
