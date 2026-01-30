package dev.sudhanshu.auth_n_authz.libs.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

public class APIException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    @Getter
    private final HttpStatusCode statusCode;

    public APIException() {
        this(
            "Something went wrong!", 
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
    
    public APIException(
        String message
    ) {
        this(
            message, 
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    public APIException(
        String message, 
        HttpStatus code
    ) {
        super(message);
        this.statusCode = code;
    }
}
