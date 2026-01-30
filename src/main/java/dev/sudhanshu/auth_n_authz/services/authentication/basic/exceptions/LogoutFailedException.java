package dev.sudhanshu.auth_n_authz.services.authentication.basic.exceptions;

import org.springframework.http.HttpStatus;

import dev.sudhanshu.auth_n_authz.libs.exceptions.APIException;

public class LogoutFailedException extends APIException {

    public LogoutFailedException() {
        super(
            "Logout attempt failed.", 
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
