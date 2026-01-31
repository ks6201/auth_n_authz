package dev.sudhanshu.auth_n_authz.controllers.v1.authentication.basic;

import org.springframework.web.bind.annotation.RestController;

import dev.sudhanshu.auth_n_authz.controllers.v1.authentication.basic.dtos.CookieOptionQueryDTO;
import dev.sudhanshu.auth_n_authz.controllers.v1.authentication.basic.dtos.BasicAuthLoginRequestDTO;
import dev.sudhanshu.auth_n_authz.controllers.v1.authentication.basic.dtos.BasicAuthLoginResponseDTO;
import dev.sudhanshu.auth_n_authz.controllers.v1.authentication.basic.dtos.BasicAuthSignupRequestDTO;
import dev.sudhanshu.auth_n_authz.controllers.v1.authentication.basic.dtos.BasicAuthSignupResponseDTO;
import dev.sudhanshu.auth_n_authz.libs.Constants;
import dev.sudhanshu.auth_n_authz.libs.Utils;
import dev.sudhanshu.auth_n_authz.libs.payload.APIResponse;
import dev.sudhanshu.auth_n_authz.services.authentication.basic.BasicAuthService;
import dev.sudhanshu.auth_n_authz.services.authentication.basic.commands.CreateUserCommand;
import dev.sudhanshu.auth_n_authz.services.authentication.basic.commands.LoginCommand;
import jakarta.validation.Valid;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/v1/auth")
public class BasicAuthController {

    @Autowired
    BasicAuthService passwordAuthService;

    @PostMapping("/stateless/login")
    public ResponseEntity<APIResponse<BasicAuthLoginResponseDTO>> statelessLogin(
        @RequestBody @Valid BasicAuthLoginRequestDTO loginCreds,
        @ModelAttribute @Valid CookieOptionQueryDTO cookieOption
    ) {
        var loginCommand = new LoginCommand(
            loginCreds.email(), 
            loginCreds.password()
        );
        var result = passwordAuthService.statelessAuthenticate(loginCommand);

        var responseDto = new BasicAuthLoginResponseDTO(
            result.jwt()
        );

        var cookieOpt = cookieOption.getCookie();

        if(cookieOpt != null && cookieOpt == true) {
            ResponseCookie sessionCookie = ResponseCookie.from(Constants.STATELESS_AUTH_COOKIE_NAME, result.jwt())
                .httpOnly(cookieOption.getHttpOnly())
                .secure(cookieOption.getSecure())
                .path(cookieOption.getPath())
                .maxAge(cookieOption.getMaxAge())
                .sameSite(cookieOption.getSameSite())
                .build();
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, sessionCookie.toString())
                    .body(APIResponse.success(null));
        }

        return ResponseEntity.ok(
            APIResponse.success(responseDto)
        );
    }

    @PostMapping("/stateful/login")
    public ResponseEntity<APIResponse<BasicAuthLoginResponseDTO>> statefulLogin(
        @RequestBody @Valid BasicAuthLoginRequestDTO loginCreds,
        @ModelAttribute @Valid CookieOptionQueryDTO cookieOption
    ) {
        var loginCommand = new LoginCommand(
            loginCreds.email(), 
            loginCreds.password()
        );
        var result = passwordAuthService.statefulAuthenticate(loginCommand);

        var responseDto = new BasicAuthLoginResponseDTO(
            result.sessionId()
        );

        var cookieOpt = cookieOption.getCookie();

        if(cookieOpt != null && cookieOpt == true) {
            ResponseCookie sessionCookie = ResponseCookie.from(Constants.STATEFUL_AUTH_COOKIE_NAME, result.sessionId())
                .httpOnly(cookieOption.getHttpOnly())
                .secure(cookieOption.getSecure())
                .path(cookieOption.getPath())
                .maxAge(cookieOption.getMaxAge())
                .sameSite(cookieOption.getSameSite())
                .build();
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, sessionCookie.toString())
                    .body(APIResponse.success(null));
        }

        return ResponseEntity.ok(
            APIResponse.success(responseDto)
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<APIResponse<BasicAuthSignupResponseDTO>> signup(
        @RequestBody @Valid BasicAuthSignupRequestDTO signupCreds
    ) {
        var createUserCommand = new CreateUserCommand(
            signupCreds.email(),
            signupCreds.password(),
            signupCreds.applicationName()
        );

        var result = this.passwordAuthService.createUser(createUserCommand);

        URI location = URI.create(
            Utils.buildUserCreatedAtRoute(result.userId().toString())
        );

        var responseDto = new BasicAuthSignupResponseDTO(
            result.userId(),
            result.email()
        );

        return ResponseEntity
            .created(location)
            .body(
                APIResponse.success(responseDto)
            );
    }
    
}
