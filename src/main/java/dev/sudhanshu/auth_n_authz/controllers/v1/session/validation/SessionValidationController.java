package dev.sudhanshu.auth_n_authz.controllers.v1.session.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;


import dev.sudhanshu.auth_n_authz.controllers.v1.session.validation.dtos.SessionValidationRequestDTO;
import dev.sudhanshu.auth_n_authz.libs.Constants;
import dev.sudhanshu.auth_n_authz.libs.Utils;
import dev.sudhanshu.auth_n_authz.libs.exceptions.http.HttpBadRequestException;
import dev.sudhanshu.auth_n_authz.libs.payload.APIResponse;
import dev.sudhanshu.auth_n_authz.services.session.validation.commands.SessionValidateCommand;
import dev.sudhanshu.auth_n_authz.services.session.validation.stateful.StatefulSessionValidationService;
import jakarta.validation.Valid;

// @RestController
@RequestMapping("/v1/auth")
public class SessionValidationController {

    @Autowired
    private StatefulSessionValidationService sessionValidationService;

    @PostMapping("/stateless/validate")
    public ResponseEntity<APIResponse<String>> statelessValidation(
        @RequestBody(required = false) @Valid SessionValidationRequestDTO sessionBody,
        @RequestHeader(value = Constants.AUTHZ_HEADER_KEY, required = false) String authzHeader,
        @CookieValue(value = Constants.STATEFUL_AUTH_COOKIE_NAME, required = false) String cookieBasedSessionId
    ) {

        String token = cookieBasedSessionId != null ? 
            cookieBasedSessionId : authzHeader != null ? 
            Utils.extractTokenFromAuthzHeader(authzHeader) : 
            (sessionBody != null ? sessionBody.token() : null);

        if(token == null) {
            throw new HttpBadRequestException(
                "No session token provided."
            );
        }

        var command = new SessionValidateCommand(token);

        this.sessionValidationService.validate(command);

        return ResponseEntity.ok(
            APIResponse.success("valid session")
        );
    }

    @PostMapping("/stateful/validate")
    public ResponseEntity<APIResponse<String>> statefulValidation(
        @RequestBody(required = false) @Valid SessionValidationRequestDTO sessionBody,
        @RequestHeader(value = Constants.AUTHZ_HEADER_KEY, required = false) String authzHeader,
        @CookieValue(value = Constants.STATEFUL_AUTH_COOKIE_NAME, required = false) String cookieBasedSessionId
    ) {

        String token = cookieBasedSessionId != null ? 
            cookieBasedSessionId : authzHeader != null ? 
            Utils.extractTokenFromAuthzHeader(authzHeader) : 
            (sessionBody != null ? sessionBody.token() : null);

        if(token == null) {
            throw new HttpBadRequestException(
                "No session token provided."
            );
        }

        var command = new SessionValidateCommand(token);
        this.sessionValidationService.validate(command);

        return ResponseEntity.ok(
            APIResponse.success("valid session")
        );
    }
}
