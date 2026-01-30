package dev.sudhanshu.auth_n_authz.services.session.validation.commands;

public record SessionValidateCommand(
    String sessionToken
) {
}
