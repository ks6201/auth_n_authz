package dev.sudhanshu.auth_n_authz.libs.security.jwt;

public interface JwtProvider {
    JwtPayload verify(String jwt);
    String sign(String claim);
}
