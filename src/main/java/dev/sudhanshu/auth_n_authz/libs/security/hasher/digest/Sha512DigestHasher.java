package dev.sudhanshu.auth_n_authz.libs.security.hasher.digest;

import java.util.HexFormat;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Primary
@Component
public class Sha512DigestHasher implements DigestHasher {
    private static final MessageDigest SHA_512;

    static {
        try {
            SHA_512 = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Override
    public String hash(String input) {

        var hashBytes = SHA_512.digest(input.getBytes(StandardCharsets.UTF_8));

        var hashHex = HexFormat.of().formatHex(hashBytes);

        return hashHex;
    }
}
