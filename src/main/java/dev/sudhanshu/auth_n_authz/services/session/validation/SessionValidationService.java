package dev.sudhanshu.auth_n_authz.services.session.validation;

import dev.sudhanshu.auth_n_authz.services.session.validation.commands.SessionValidateCommand;
import dev.sudhanshu.auth_n_authz.services.session.validation.results.ValidationResult;

public interface SessionValidationService<T> {
    ValidationResult<T> validate(SessionValidateCommand sessionValidateCommand);
    // void validateStatefulSession(SessionValidateCommand SessionValidateCommand);
    // String validateStatelessSession(SessionValidateCommand SessionValidateCommand);
}
