package dev.sudhanshu.auth_n_authz.repositories.session.redis;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.sudhanshu.auth_n_authz.libs.Constants;
import dev.sudhanshu.auth_n_authz.libs.Utils;
import dev.sudhanshu.auth_n_authz.libs.redis.Redis;
import dev.sudhanshu.auth_n_authz.repositories.session.SessionRepository;

@Component
public class RedisSessionRepository implements SessionRepository {

    @Autowired
    private Redis redis;

    private final String KEY_PREFIX = "stateful-session";

    public boolean store(
        String sessionId,
        String claim
    ) {
        var key = Utils.buildRedisKey(this.KEY_PREFIX, sessionId);

        return this.redis.set(key, claim, Constants.COOKIE_EXPIRY_MS);
    }
    
    public boolean deleteSession(
        String sessionId
    ) {
        var key = Utils.buildRedisKey(this.KEY_PREFIX, sessionId);
        return this.redis.delete(key);
    }
    
    @Override
    public Optional<String> get(String sessionId) {
        var key = Utils.buildRedisKey(this.KEY_PREFIX, sessionId);
        return this.redis.get(key, String.class);
    }

}
