package dev.sudhanshu.auth_n_authz.services.session.validation.stateless;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import dev.sudhanshu.auth_n_authz.libs.exceptions.APIException;
import dev.sudhanshu.auth_n_authz.libs.security.jwt.JwtProvider;
import dev.sudhanshu.auth_n_authz.services.session.validation.SessionValidationService;
import dev.sudhanshu.auth_n_authz.services.session.validation.commands.SessionValidateCommand;
import dev.sudhanshu.auth_n_authz.services.session.validation.exceptions.InvalidTokenException;
import dev.sudhanshu.auth_n_authz.services.session.validation.exceptions.TokenExpiredException;
import dev.sudhanshu.auth_n_authz.services.session.validation.results.ValidationResult;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@Service
public class StatelessSessionValidationService implements SessionValidationService<String> {

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public ValidationResult<String> validate(SessionValidateCommand sessionValidateCommand) {
        try {
            var claims = jwtProvider.verify(sessionValidateCommand.sessionToken());

            return new ValidationResult<>(claims.subject());

        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException();
        } catch (MalformedJwtException | IllegalArgumentException e) {
            throw new InvalidTokenException();
        } catch (Exception e) {
            throw new APIException(
                "Unexpected error validating token", 
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

}
