package dev.sudhanshu.auth_n_authz.services.authentication.basic.commands;

public record CreateUserCommand(
    String email,
    String password,
    String applicationName
) {
}
