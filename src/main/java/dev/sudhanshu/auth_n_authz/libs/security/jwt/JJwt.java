package dev.sudhanshu.auth_n_authz.libs.security.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.sudhanshu.auth_n_authz.configs.JwtKeyProvider;
import io.jsonwebtoken.Jwts;

@Component
public class JJwt implements JwtProvider {

    @Autowired
    private JwtKeyProvider jwtKeyProvider;

    private long EXP_TIME_MS = 60 * 60 * 1000;

    @Override
    public JwtPayload verify(String jwt) {
        var payload = Jwts.parser()
                .verifyWith(this.jwtKeyProvider.getKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
        
        var jwtPayload = new JwtPayload(
            payload.getSubject(),
            payload.getIssuer(),
            payload.getExpiration()
        );

        return jwtPayload;
    }
    
    @Override
    public String sign(String claim) {
        return Jwts.builder()
                    .subject(claim)
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + this.EXP_TIME_MS))
                    .signWith(this.jwtKeyProvider.getKey(), Jwts.SIG.HS256)
                    .compact();
     }
}
