package dev.sudhanshu.auth_n_authz.services.session.validation.results;


public record ValidationResult<T>(
    T claim
) {
}