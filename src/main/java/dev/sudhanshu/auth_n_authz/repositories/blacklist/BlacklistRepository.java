package dev.sudhanshu.auth_n_authz.repositories.blacklist;

public interface BlacklistRepository {
    boolean blacklist(String token);
    boolean isBlacklisted(String token);
}
