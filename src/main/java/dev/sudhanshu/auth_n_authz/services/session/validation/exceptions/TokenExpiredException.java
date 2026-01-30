package dev.sudhanshu.auth_n_authz.services.session.validation.exceptions;

import org.springframework.http.HttpStatus;

import dev.sudhanshu.auth_n_authz.libs.exceptions.APIException;

public class TokenExpiredException extends APIException {
    public TokenExpiredException() {
        super("Authentication token expired", HttpStatus.UNAUTHORIZED);
    }
}
