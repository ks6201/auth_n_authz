package dev.sudhanshu.auth_n_authz.libs.exceptions;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.sudhanshu.auth_n_authz.libs.payload.APIResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class.getName());

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<String>> exceptionHandler(
        Exception exception
    ) {
        String message = "An unexpected error occurred while processing your request. Please try again later.";

        APIResponse<String> response = APIResponse.error(message);

        exception.printStackTrace();

        // Add exception class info so it can be handled if needed.
        logger.severe(exception.getMessage());
        
        return new ResponseEntity<>(
            response,
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<Map<String, String>>> dtoValidationFailedException(
        MethodArgumentNotValidException exception
    ) {

        Map<String, String> response = new HashMap<>();

        exception.getBindingResult().getAllErrors().forEach(err -> {
            
            String fieldName = ((FieldError) err).getField();
            String message = err.getDefaultMessage();

            response.put(fieldName, message);
        });

        APIResponse<Map<String, String>> apiResponse = 
            new APIResponse<>(response, true);

        return new ResponseEntity<>(
            apiResponse,
            HttpStatus.BAD_REQUEST
        );
    }
    
    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse<String>> apiExceptionHandler(
        APIException exception
    ) {
        String message = exception.getMessage();

        logger.severe(message);

        APIResponse<String> response = APIResponse.error(message);

        return new ResponseEntity<>(
            response,
            exception.getStatusCode()
        );
    }
}
