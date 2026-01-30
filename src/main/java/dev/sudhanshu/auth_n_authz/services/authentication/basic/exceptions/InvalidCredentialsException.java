package dev.sudhanshu.auth_n_authz.services.authentication.basic.exceptions;

import org.springframework.http.HttpStatus;

import dev.sudhanshu.auth_n_authz.libs.exceptions.APIException;

public class InvalidCredentialsException extends APIException {

    public InvalidCredentialsException() {
        super("Invalid email or password", HttpStatus.UNAUTHORIZED);
    }
}
