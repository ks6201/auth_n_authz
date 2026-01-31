package dev.sudhanshu.auth_n_authz.libs;

import java.util.Optional;
import dev.sudhanshu.auth_n_authz.libs.security.hasher.digest.DigestHasher;
import dev.sudhanshu.auth_n_authz.libs.security.hasher.digest.Sha512DigestHasher;

public class Utils {

    private static final DigestHasher DIGEST_HASHER = new Sha512DigestHasher();

    public static String buildUserCreatedAtRoute(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId must not be null or blank");
        }
        return "/v1/users/" + userId;
    }

    public static String buildRedisKey(
        String prefix,
        String uniqueValue
    ) {
        return DIGEST_HASHER.hash(prefix + ":" + uniqueValue);
    }

    public static Optional<String> extractTokenFromAuthzHeaderValue(String header) {
        if (header == null) {
            return Optional.empty();
        }

        if (!header.startsWith("Bearer ")) {
            return Optional.empty();
        }

        String token = header.substring(7).trim();

        return token.isEmpty()
                ? Optional.empty()
                : Optional.of(token);
    }
}
