package dev.sudhanshu.auth_n_authz.libs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class UtilsTests {

    @Test
    void buildRedisKey_shouldReturnDeterministicHash() {
        String result1 = Utils.buildRedisKey("user", "123");
        String result2 = Utils.buildRedisKey("user", "123");

        assertEquals(result1, result2);
        assertNotEquals("user:123", result1);
        assertFalse(result1.isBlank());
    }

    @Test
    void extractTokenFromAuthzHeaderValue_shouldReturnTokenString() {
        var header = "Bearer token";

        var token = Utils.extractTokenFromAuthzHeaderValue(header);

        assertTrue(token.isPresent());
        assertEquals("token", token.get());
    }

    @Test
    void extractTokenFromAuthzHeaderValue_shouldReturnEmpty_whenHeaderIsNull() {
        String header = null;

        var token = Utils.extractTokenFromAuthzHeaderValue(header);

        assertNotNull(token);
        assertTrue(token.isEmpty());
    }

        
    @Test
    void extractTokenFromAuthzHeaderValue_shouldReturnEmpty_whenHeaderIsMalformed() {
        var header = "Bearer-token";

        var token = Utils.extractTokenFromAuthzHeaderValue(header);

        assertNotNull(token);
        assertTrue(token.isEmpty());
    }

    @Test
    void buildUserCreatedAtRoute_shouldConstructUrlWithUserId() {
        String userId = "123";

        String result = Utils.buildUserCreatedAtRoute(userId);

        assertEquals("/v1/users/123", result);
    }

    @Test
    void buildUserCreatedAtRoute_shouldThrowException_whenUserIdIsNull() {
        assertThrows(
            IllegalArgumentException.class,
            () -> Utils.buildUserCreatedAtRoute(null)
        );
    }

    @Test
    void buildUserCreatedAtRoute_shouldThrowException_whenUserIdIsBlank() {
        assertThrows(
            IllegalArgumentException.class,
            () -> Utils.buildUserCreatedAtRoute("   ")
        );
    }

}
