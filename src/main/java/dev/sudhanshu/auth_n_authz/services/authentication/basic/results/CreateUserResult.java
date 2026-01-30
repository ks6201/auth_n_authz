package dev.sudhanshu.auth_n_authz.services.authentication.basic.results;

import java.util.UUID;

public record CreateUserResult(
    UUID userId,
    String email
) {
}
