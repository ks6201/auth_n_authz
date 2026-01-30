package dev.sudhanshu.auth_n_authz.services.authentication.basic.exceptions;

import org.springframework.http.HttpStatus;

import dev.sudhanshu.auth_n_authz.libs.exceptions.APIException;

public class UserNotFoundException extends APIException {

    public UserNotFoundException(
        String email
    ) {
        super(
            "User having email '" + email + "' does not exists.",
            HttpStatus.NOT_FOUND
        );
    }
}
