package dev.sudhanshu.auth_n_authz.services.authentication.magiclink;

import dev.sudhanshu.auth_n_authz.services.authentication.magiclink.commands.CompleteMagicAuthSessionCommand;
import dev.sudhanshu.auth_n_authz.services.authentication.magiclink.commands.CreateMagicLinkAuthSessionCommand;
import dev.sudhanshu.auth_n_authz.services.authentication.magiclink.results.CompleteMagicAuthSessionResult;

public interface MagicLinkAuthService {
    void createMagicAuthSession(CreateMagicLinkAuthSessionCommand createMagicLinkAuthSessionCmd);
    CompleteMagicAuthSessionResult completeMagicAuthSession(
        CompleteMagicAuthSessionCommand cmd
    );
}
