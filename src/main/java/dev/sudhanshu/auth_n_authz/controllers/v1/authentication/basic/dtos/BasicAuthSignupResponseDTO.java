package dev.sudhanshu.auth_n_authz.controllers.v1.authentication.basic.dtos;

import java.util.UUID;

public record BasicAuthSignupResponseDTO(
    UUID userId,
    String email
) {
}
