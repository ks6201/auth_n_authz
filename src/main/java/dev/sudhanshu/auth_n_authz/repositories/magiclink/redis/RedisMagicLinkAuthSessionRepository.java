package dev.sudhanshu.auth_n_authz.repositories.magiclink.redis;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.sudhanshu.auth_n_authz.libs.Utils;
import dev.sudhanshu.auth_n_authz.libs.redis.Lettuce;
import dev.sudhanshu.auth_n_authz.repositories.magiclink.MagicLinkAuthSessionData;
import dev.sudhanshu.auth_n_authz.repositories.magiclink.MagicLinkAuthSessionRepository;

@Component
public class RedisMagicLinkAuthSessionRepository implements MagicLinkAuthSessionRepository {

    @Autowired
    private Lettuce lettuce;

    private final String KEY_PREFIX = "magic-auth-session";

    @Override
    public boolean create(MagicLinkAuthSessionData magicLinkAuthSessionData) {
        var id = magicLinkAuthSessionData.sessionId();
        var key = Utils.buildRedisKey(KEY_PREFIX, id);
        
        return lettuce.set(key, magicLinkAuthSessionData);
    }

    @Override
    public Optional<MagicLinkAuthSessionData> getSession(String magicId) {
        var key = Utils.buildRedisKey(KEY_PREFIX, magicId);
        var session = lettuce.get(key, MagicLinkAuthSessionData.class);
        
        return session;
    }
    
    @Override
    public boolean exists(String magicId) {
        var key = Utils.buildRedisKey(KEY_PREFIX, magicId);
        
        return lettuce.exists(key);
    }
    
    @Override
    public boolean deleteSession(String magicId) {
        var key = Utils.buildRedisKey(KEY_PREFIX, magicId);

        return lettuce.delete(key);
    }
}
