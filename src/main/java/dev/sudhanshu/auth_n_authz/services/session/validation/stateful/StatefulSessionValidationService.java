package dev.sudhanshu.auth_n_authz.services.session.validation.stateful;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.sudhanshu.auth_n_authz.repositories.session.SessionRepository;
import dev.sudhanshu.auth_n_authz.services.session.validation.SessionValidationService;
import dev.sudhanshu.auth_n_authz.services.session.validation.commands.SessionValidateCommand;
import dev.sudhanshu.auth_n_authz.services.session.validation.exceptions.SessionValidationFailedException;
import dev.sudhanshu.auth_n_authz.services.session.validation.results.ValidationResult;

@Service
public class StatefulSessionValidationService implements SessionValidationService<String> {
    
    @Autowired
    private SessionRepository sessionRepository;

    @Override
    public ValidationResult<String> validate(SessionValidateCommand sessionValidateCommand) {
        var claimOpt = this.sessionRepository.get(sessionValidateCommand.sessionToken());

        if(claimOpt.isEmpty()) {
            throw new SessionValidationFailedException();
        }
        
        return new ValidationResult<>(
            claimOpt.get()
        );
    }

}
