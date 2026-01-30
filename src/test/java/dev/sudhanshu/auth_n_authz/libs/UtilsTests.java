package dev.sudhanshu.auth_n_authz.libs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilsTests {

    @Test
    void buildRedisKey_shouldConcatenatePrefixAndValueWithColon() {
        String result = Utils.buildRedisKey("user", "123");

        assertEquals("user:123", result);
    }
}
