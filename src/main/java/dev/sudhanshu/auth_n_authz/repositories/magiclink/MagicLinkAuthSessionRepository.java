package dev.sudhanshu.auth_n_authz.repositories.magiclink;

import java.util.Optional;

public interface MagicLinkAuthSessionRepository {
    boolean create(MagicLinkAuthSessionData magicLinkAuthSessionData);
    Optional<MagicLinkAuthSessionData> getSession(String magicId);
    boolean deleteSession(String magicId);
    boolean exists(String magicId);
}
