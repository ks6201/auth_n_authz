package dev.sudhanshu.auth_n_authz.libs;

import java.util.function.Function;
import dev.sudhanshu.auth_n_authz.libs.security.hasher.digest.DigestHasher;
import dev.sudhanshu.auth_n_authz.libs.security.hasher.digest.Sha512DigestHasher;

public class Utils {

    private static final DigestHasher DIGEST_HASHER = new Sha512DigestHasher();

    public static Function<String, String> USER_CREATED_AT_ROUTE = (String userId) -> {
        return "/v1/users/" + userId;
    };

    public static String buildRedisKey(
        String prefix,
        String value
    ) {
        return DIGEST_HASHER.hash(prefix + ":" + value);
    }

    public static String extractTokenFromAuthzHeader(
        String header
    ) {
        if(header == null) return null;
        return header.split(" ")[1];
    }
}
