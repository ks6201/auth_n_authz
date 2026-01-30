package dev.sudhanshu.auth_n_authz.libs.redis;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.lettuce.core.api.StatefulRedisConnection;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class Lettuce implements Redis {

    @Autowired
    private StatefulRedisConnection<String, byte[]> redisConn;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public boolean set(String key, Object value) {
        try {
            byte[] bytes = MAPPER.writeValueAsBytes(value);
            return "OK".equals(redisConn.sync().set(key, bytes));
        } catch (Exception e) {
            throw new RuntimeException("Serialization failed", e);
        }
    }

    @Override
    public boolean set(String key, Object value, long ttlSec) {
        try {
            byte[] bytes = MAPPER.writeValueAsBytes(value);
            return "OK".equals(redisConn.sync().setex(key, ttlSec, bytes));
        } catch (Exception e) {
            throw new RuntimeException("Serialization failed", e);
        }
    }

    @Override
    public <T> Optional<T> get(String key, Class<T> type) {
        try {
            byte[] bytes = redisConn.sync().get(key);
            if (bytes == null) return Optional.empty();
            return Optional.of(MAPPER.readValue(bytes, type));
        } catch (Exception e) {
            throw new RuntimeException("Deserialization failed", e);
        }
    }

    @Override
    public boolean delete(String key) {
        return redisConn.sync().del(key) > 0;
    }

    @Override
    public boolean exists(String key) {
        return redisConn.sync().exists(key) > 0;
    }
}

