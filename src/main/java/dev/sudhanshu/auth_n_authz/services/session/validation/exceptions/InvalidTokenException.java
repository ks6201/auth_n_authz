package dev.sudhanshu.auth_n_authz.services.session.validation.exceptions;

import org.springframework.http.HttpStatus;

import dev.sudhanshu.auth_n_authz.libs.exceptions.APIException;

public class InvalidTokenException extends APIException {
    public InvalidTokenException() {
        super("Invalid session token.", HttpStatus.BAD_REQUEST);
    }
}
