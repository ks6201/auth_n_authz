package dev.sudhanshu.auth_n_authz.services.authentication.basic;


import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import dev.sudhanshu.auth_n_authz.entities.Application;
import dev.sudhanshu.auth_n_authz.entities.User;
import dev.sudhanshu.auth_n_authz.libs.security.hasher.password.PasswordHasher;
import dev.sudhanshu.auth_n_authz.libs.security.jwt.JwtProvider;
import dev.sudhanshu.auth_n_authz.repositories.application.jpa.ApplicationRepository;
import dev.sudhanshu.auth_n_authz.repositories.blacklist.BlacklistRepository;
import dev.sudhanshu.auth_n_authz.repositories.session.SessionRepository;
import dev.sudhanshu.auth_n_authz.repositories.user.jpa.UserRepository;
import dev.sudhanshu.auth_n_authz.services.authentication.basic.commands.CreateUserCommand;
import dev.sudhanshu.auth_n_authz.services.authentication.basic.commands.LoginCommand;
import dev.sudhanshu.auth_n_authz.services.authentication.basic.commands.LogoutCommand;
import dev.sudhanshu.auth_n_authz.services.authentication.basic.exceptions.InvalidCredentialsException;
import dev.sudhanshu.auth_n_authz.services.authentication.basic.exceptions.LogoutFailedException;
import dev.sudhanshu.auth_n_authz.services.authentication.basic.exceptions.UserAlreadyExistsException;
import dev.sudhanshu.auth_n_authz.services.authentication.basic.exceptions.UserAlreadyLoggedOutException;
import dev.sudhanshu.auth_n_authz.services.authentication.basic.exceptions.UserNotFoundException;
import dev.sudhanshu.auth_n_authz.services.authentication.basic.results.CreateUserResult;
import dev.sudhanshu.auth_n_authz.services.authentication.basic.results.StatefulLoginResult;
import dev.sudhanshu.auth_n_authz.services.authentication.basic.results.StatelessLoginResult;
import dev.sudhanshu.auth_n_authz.services.session.validation.stateless.StatelessSessionValidationService;
import jakarta.transaction.Transactional;

@Service
public class BasicAuthServiceImpl implements BasicAuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    PasswordHasher passwordHasher;

    @Autowired
    BlacklistRepository blacklistRepository;

    @Autowired
    JwtProvider  jwtProvider;

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    StatelessSessionValidationService statelessSessionValidationService;


    private static final Logger logger = Logger.getLogger(BasicAuthServiceImpl.class.getName());

    @Override
    @Transactional
    public CreateUserResult createUser(CreateUserCommand command) {

        final String hashedPassword = passwordHasher.hash(command.password());

        Application application = this.getOrCreateApplication(command.applicationName());

        User user = new User();
        user.setEmail(command.email());
        user.setPassword(hashedPassword);
        user.setApplication(application);

        try {
            User saved = userRepository.save(user);

            return new CreateUserResult(
                saved.getUserId(),
                saved.getEmail()
            );

        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException(command.email());
        }
    }


    private Application getOrCreateApplication(String applicationName) {

        Optional<Application> existing =
            applicationRepository.findByApplicationName(applicationName);

        if (existing.isPresent()) {
            return existing.get();
        }

        Application application = new Application();
        application.setApplicationName(applicationName);

        try {
            return applicationRepository.save(application);
        } catch (DataIntegrityViolationException e) {

            return applicationRepository
                .findByApplicationName(applicationName)
                .orElseThrow(() ->
                    new IllegalStateException(
                        "Application was created concurrently but cannot be found: " + applicationName,
                        e
                    )
                );
        }
    }


    @Override
    public StatelessLoginResult statelessAuthenticate(LoginCommand loginCommand) {
        User user = this.userRepository
            .findByEmail(loginCommand.email())
            .orElseThrow(() -> new UserNotFoundException(loginCommand.email()));
        
        var wasSuccess = passwordHasher
            .verify(loginCommand.password(), user.getPassword());

        if(!wasSuccess) {
            BasicAuthServiceImpl.logger.info(
                "Credential didn't match for email: '" + loginCommand.email() + "'."
            );
            throw new InvalidCredentialsException();
        }
        
        var token = createStatelessToken(user.getEmail());

        return new StatelessLoginResult(token);
    }

    @Override
    public String createStatelessToken(String claim) {
        var token = this.jwtProvider.sign(claim);

        return token;
    }


    @Override
    public StatefulLoginResult statefulAuthenticate(LoginCommand loginCommand) {
        User user = this.userRepository
            .findByEmail(loginCommand.email())
            .orElseThrow(() -> new UserNotFoundException(loginCommand.email()));

        var wasSuccess = passwordHasher
            .verify(loginCommand.password(), user.getPassword());

        if(!wasSuccess) {
            BasicAuthServiceImpl.logger.info(
                "Credential didn't match for email: '" + loginCommand.email() + "'."
            );
            throw new InvalidCredentialsException();
        }

        var sessionId = createStatefulSession(loginCommand.email());

        return new StatefulLoginResult(sessionId);
    }

    @Override
    public String createStatefulSession(String claim) {
        var sessionId = UUID.randomUUID().toString();
        
        this.sessionRepository.store(sessionId, claim);

        return sessionId;
    }

    @Override
    public void statelessLogout(LogoutCommand logoutCommand) {

        var isBlacklisted = this.blacklistRepository.isBlacklisted(logoutCommand.sessionToken());

        if(isBlacklisted) {
            BasicAuthServiceImpl.logger.info(
                "User having with token '" + logoutCommand.sessionToken() + "' already logged out."
            );
            throw new UserAlreadyLoggedOutException();
        }

        var wasSuccess = this.blacklistRepository.blacklist(logoutCommand.sessionToken());

        if(wasSuccess) return;
    
        BasicAuthServiceImpl.logger.info(
            "User having with token '" + logoutCommand.sessionToken() + "' logged out attempt failed."
        );

        throw new LogoutFailedException();
    }

    @Override
    public void statefulLogout(LogoutCommand logoutCommand) {

        var wasSuccess = this.sessionRepository.deleteSession(logoutCommand.sessionToken());

        if(wasSuccess) return;

        BasicAuthServiceImpl.logger.info(
            "User having with token '" + logoutCommand.sessionToken() + "' logged out attempt failed."
        );

        throw new LogoutFailedException();
    }
}
