package dev.sudhanshu.auth_n_authz.filters.session;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
// import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import dev.sudhanshu.auth_n_authz.libs.Constants;
import dev.sudhanshu.auth_n_authz.libs.Utils;
import dev.sudhanshu.auth_n_authz.libs.exceptions.http.HttpUnauthorizedException;
import dev.sudhanshu.auth_n_authz.services.session.validation.commands.SessionValidateCommand;
import dev.sudhanshu.auth_n_authz.services.session.validation.exceptions.SessionValidationFailedException;
import dev.sudhanshu.auth_n_authz.services.session.validation.results.ValidationResult;
import dev.sudhanshu.auth_n_authz.services.session.validation.stateful.StatefulSessionValidationService;
import dev.sudhanshu.auth_n_authz.services.session.validation.stateless.StatelessSessionValidationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// @Component
public class SessionFilter extends OncePerRequestFilter {

    @Autowired
    StatefulSessionValidationService statefulSessionValidationService;

    @Autowired
    StatelessSessionValidationService statelessSessionValidationService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        SessionTokenExtractionResult result = extractFromCookieOrHeader(request);

        String token = result.getToken();

        if (!StringUtils.hasText(token)) {
            throw new HttpUnauthorizedException();
        }

        SessionValidateCommand command = new SessionValidateCommand(token);

        var validationResult = switch (result.getType()) {
            case SessionTokenType.STATEFUL -> statefulSessionValidationService.validate(command);
            case SessionTokenType.STATELESS -> statelessSessionValidationService.validate(command);
            case SessionTokenType.UNKNOWN -> tryStatefulThenStateless(command);
        };

        request.setAttribute("user", validationResult.claim());

        filterChain.doFilter(request, response);
    }

    private ValidationResult<String> tryStatefulThenStateless(SessionValidateCommand command) {
        try {
            return statefulSessionValidationService.validate(command);
        } catch (SessionValidationFailedException ex) {
            return statelessSessionValidationService.validate(command);
        }
    }


    private SessionTokenExtractionResult extractFromCookieOrHeader(HttpServletRequest request) {

        // 1️⃣ Stateful cookie
        Cookie cookie = WebUtils.getCookie(
                request,
                Constants.STATEFUL_AUTH_COOKIE_NAME
        );

        if (cookie != null && StringUtils.hasText(cookie.getValue())) {
            return new SessionTokenExtractionResult(
                    cookie.getValue(),
                    SessionTokenType.STATEFUL
            );
        }

        // 2️⃣ Stateless cookie
        cookie = WebUtils.getCookie(
                request,
                Constants.STATELESS_AUTH_COOKIE_NAME
        );

        if (cookie != null && StringUtils.hasText(cookie.getValue())) {
            return new SessionTokenExtractionResult(
                    cookie.getValue(),
                    SessionTokenType.STATELESS
            );
        }

        // 3️⃣ Authorization header
        String token = Utils.extractTokenFromAuthzHeaderValue(
                request.getHeader(HttpHeaders.AUTHORIZATION)
        ).orElseThrow(() -> new HttpUnauthorizedException());

        return new SessionTokenExtractionResult(token, SessionTokenType.UNKNOWN);
    }
}
