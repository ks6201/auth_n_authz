package dev.sudhanshu.auth_n_authz.libs.redis;

import java.util.Optional;

public interface Redis {
    boolean delete(String key);
    boolean exists(String key);
    <T> Optional<T> get(String key, Class<T> type);
    boolean set(String key, Object value);
    boolean set(String key, Object value, long ttl);
}