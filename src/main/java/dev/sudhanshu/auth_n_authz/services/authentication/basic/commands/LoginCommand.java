package dev.sudhanshu.auth_n_authz.services.authentication.basic.commands;

public record LoginCommand(
    String email,
    String password
) {
}
