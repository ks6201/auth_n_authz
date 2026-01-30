package dev.sudhanshu.auth_n_authz.repositories.session;

import java.util.Optional;

public interface SessionRepository {
    boolean store(String sessionId);
    Optional<String> get(String sessionId);
    boolean deleteSession(String sessionId);
}
