package dev.sudhanshu.auth_n_authz.repositories.blacklist.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.sudhanshu.auth_n_authz.libs.Utils;
import dev.sudhanshu.auth_n_authz.libs.redis.Lettuce;
import dev.sudhanshu.auth_n_authz.libs.security.jwt.JwtProvider;
import dev.sudhanshu.auth_n_authz.repositories.blacklist.BlacklistRepository;

@Component
public class RedisBlacklistRepository implements BlacklistRepository {

    @Autowired
    private Lettuce lettuce;

    @Autowired
    private JwtProvider jwtProvider;

    private final String KEY_PREFIX = "blacklisted-jwt";

    @Override
    public boolean blacklist(String token) {
        var claims = jwtProvider.verify(token);

        long now = System.currentTimeMillis();
        long expiration = claims.expiration().getTime();

        long remainingTtlSeconds = (expiration - now) / 1000;

        // this scenario a success, since the token won't be usable anymore.
        if (remainingTtlSeconds <= 0) {
            return true;
        }

        var key = Utils.buildRedisKey(KEY_PREFIX, token);
        return lettuce.set(key, "blacklisted", remainingTtlSeconds);
    }

    @Override
    public boolean isBlacklisted(String token) {
        var key = Utils.buildRedisKey(this.KEY_PREFIX, token);
        return this.lettuce.exists(key);
    }
}
