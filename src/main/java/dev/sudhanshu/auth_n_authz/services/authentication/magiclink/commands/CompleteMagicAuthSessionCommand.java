package dev.sudhanshu.auth_n_authz.services.authentication.magiclink.commands;

public record CompleteMagicAuthSessionCommand(
    String magicId
) {

}
