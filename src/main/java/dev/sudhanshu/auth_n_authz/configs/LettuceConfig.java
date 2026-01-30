package dev.sudhanshu.auth_n_authz.configs;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class LettuceConfig {

    @Bean(destroyMethod = "shutdown")
    RedisClient redisClient(
            @Value("${redis.uri}") String redisUri
    ) {
        return RedisClient.create(redisUri);
    }

    @Bean(destroyMethod = "close")
    StatefulRedisConnection<String, byte[]> redisConnection(
            RedisClient redisClient
    ) {
        RedisCodec<String, byte[]> codec =
                RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE);

        return redisClient.connect(codec);
    }
}