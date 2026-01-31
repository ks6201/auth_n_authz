package dev.sudhanshu.auth_n_authz.repositories.magiclink;

import dev.sudhanshu.auth_n_authz.services.authentication.magiclink.commands.AuthReqType;

public record MagicLinkAuthSessionData(
    String claim,
    String sessionId,
    AuthReqType authReqType
) {

}
