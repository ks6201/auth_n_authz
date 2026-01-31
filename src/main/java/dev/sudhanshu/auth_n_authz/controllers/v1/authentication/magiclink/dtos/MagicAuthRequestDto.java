package dev.sudhanshu.auth_n_authz.controllers.v1.authentication.magiclink.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MagicAuthRequestDto(
    @Email
    @NotBlank
    String email
) {

}
