package dev.sudhanshu.auth_n_authz.libs.security.hasher.password;

public interface PasswordHasher {
    String hash(String rawPassword);
    boolean verify(String rawPassword, String hashedPassword);
}
