package dev.sudhanshu.auth_n_authz.controllers.v1.authentication.basic.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record BasicAuthSignupRequestDTO(
    @Email
    @NotBlank
    String email,

    @NotBlank
    String password,

    @NotBlank
    String applicationName
) {
}
