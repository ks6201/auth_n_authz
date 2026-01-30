package dev.sudhanshu.auth_n_authz.services.authentication.basic.exceptions;

import org.springframework.http.HttpStatus;

import dev.sudhanshu.auth_n_authz.libs.exceptions.APIException;

public class UserAlreadyLoggedOutException extends APIException {

    public UserAlreadyLoggedOutException() {
        super(
            "User already been logged out.", 
            HttpStatus.UNAUTHORIZED
        );
    }
}
