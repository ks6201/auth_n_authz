package dev.sudhanshu.auth_n_authz.libs.security.jwt;

import java.util.Date;

public record JwtPayload(
    String subject,
    String issuer,
    Date expiration
) {

}
