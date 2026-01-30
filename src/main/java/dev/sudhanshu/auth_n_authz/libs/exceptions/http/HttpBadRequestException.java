package dev.sudhanshu.auth_n_authz.libs.exceptions.http;

import org.springframework.http.HttpStatus;

import dev.sudhanshu.auth_n_authz.libs.exceptions.APIException;

public class HttpBadRequestException extends APIException {

    public HttpBadRequestException() {
        super("HttpBadRequest", HttpStatus.BAD_REQUEST);
    }

    public HttpBadRequestException(
         String message
    ) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
