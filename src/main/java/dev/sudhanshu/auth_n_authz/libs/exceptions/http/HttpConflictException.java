package dev.sudhanshu.auth_n_authz.libs.exceptions.http;

import org.springframework.http.HttpStatus;

import dev.sudhanshu.auth_n_authz.libs.exceptions.APIException;

public class HttpConflictException extends APIException {

    public HttpConflictException() {
        super("HttpConflict", HttpStatus.BAD_REQUEST);
    }

    public HttpConflictException(
         String message
    ) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
