package dev.sudhanshu.auth_n_authz.libs.exceptions.http;

import org.springframework.http.HttpStatus;

import dev.sudhanshu.auth_n_authz.libs.exceptions.APIException;

public class HttpUnauthorizedException extends APIException {

    public HttpUnauthorizedException() {
        super("HttpUnauthorized", HttpStatus.UNAUTHORIZED);
    }

    public HttpUnauthorizedException(
         String message
    ) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
