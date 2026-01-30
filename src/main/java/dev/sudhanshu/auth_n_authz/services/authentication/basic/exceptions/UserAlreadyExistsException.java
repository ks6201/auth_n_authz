package dev.sudhanshu.auth_n_authz.services.authentication.basic.exceptions;

import dev.sudhanshu.auth_n_authz.libs.exceptions.http.HttpConflictException;

public class UserAlreadyExistsException extends HttpConflictException {

    public UserAlreadyExistsException(
        String email
    ) {
        super("User having email '" + email + "' already exists.");
    }

}
