package dev.sudhanshu.auth_n_authz.services.authentication.magiclink.commands;

public record CreateMagicLinkAuthSessionCommand(
    String email,
    AuthReqType authReqType
) {

}
