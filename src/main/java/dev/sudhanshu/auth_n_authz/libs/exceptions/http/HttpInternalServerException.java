package dev.sudhanshu.auth_n_authz.libs.exceptions.http;

import org.springframework.http.HttpStatus;

import dev.sudhanshu.auth_n_authz.libs.exceptions.APIException;

public class HttpInternalServerException extends APIException {

    public HttpInternalServerException() {
        super("HttpInternalServer", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public HttpInternalServerException(
         String message
    ) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
