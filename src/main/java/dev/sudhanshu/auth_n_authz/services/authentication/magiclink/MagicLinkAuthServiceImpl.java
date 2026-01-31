package dev.sudhanshu.auth_n_authz.services.authentication.magiclink;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.sudhanshu.auth_n_authz.configs.BackendConfig;
import dev.sudhanshu.auth_n_authz.controllers.v1.authentication.magiclink.endpoints.MagicAuthEndpoints;
import dev.sudhanshu.auth_n_authz.libs.exceptions.http.HttpBadRequestException;
import dev.sudhanshu.auth_n_authz.libs.exceptions.http.HttpInternalServerException;
import dev.sudhanshu.auth_n_authz.repositories.magiclink.MagicLinkAuthSessionData;
import dev.sudhanshu.auth_n_authz.repositories.magiclink.MagicLinkAuthSessionRepository;
import dev.sudhanshu.auth_n_authz.services.authentication.basic.BasicAuthService;
import dev.sudhanshu.auth_n_authz.services.authentication.basic.commands.CreateUserCommand;
import dev.sudhanshu.auth_n_authz.services.authentication.magiclink.commands.AuthReqType;
import dev.sudhanshu.auth_n_authz.services.authentication.magiclink.commands.CompleteMagicAuthSessionCommand;
import dev.sudhanshu.auth_n_authz.services.authentication.magiclink.commands.CreateMagicLinkAuthSessionCommand;
import dev.sudhanshu.auth_n_authz.services.authentication.magiclink.results.CompleteMagicAuthSessionResult;
import dev.sudhanshu.auth_n_authz.services.notification.email.EmailNotificationService;
import dev.sudhanshu.auth_n_authz.services.notification.email.SendEmailCommand;
import dev.sudhanshu.auth_n_authz.services.user.UserService;

@Service
public class MagicLinkAuthServiceImpl implements MagicLinkAuthService {

    @Autowired
    BackendConfig backendConfig;

    @Autowired
    BasicAuthService basicAuthService;

    @Autowired
    UserService userService;
    
    @Autowired
    EmailNotificationService emailNotificationService;

    @Autowired
    MagicLinkAuthSessionRepository magicLinkAuthSessionRepository;

    @Override
    public void createMagicAuthSession(
        CreateMagicLinkAuthSessionCommand createMagicLinkAuthSessionCmd
    ) {
        var email = createMagicLinkAuthSessionCmd.email();
        
        var doesExists = userService.exists(email);

        if(!doesExists) {
            basicAuthService.createUser(new CreateUserCommand(
                email,
                "replace-me-with-random-password-generator",
                "replace-me-with-random-appname-generator"
            ));
        }

        var sessionId = UUID.randomUUID().toString();

        var wasSuccess = magicLinkAuthSessionRepository.create(
            new MagicLinkAuthSessionData(
                email,
                sessionId,
                createMagicLinkAuthSessionCmd.authReqType()
            )
        );

        if(!wasSuccess) {
            throw new HttpInternalServerException();
        }

        var backendUri = backendConfig.getBackendUri();

        var magicLink = String.format(
            "%s" + MagicAuthEndpoints.BASE + "/%s",
            backendUri,
            sessionId
        );

        var cmd = new SendEmailCommand(
            email, 
            "MagicLink Authentication", 
            "Magic link: " + magicLink
        );

        wasSuccess = emailNotificationService.send(cmd);

        if(!wasSuccess) {
            throw new HttpInternalServerException();
        }
    }


    @Override
    public CompleteMagicAuthSessionResult completeMagicAuthSession(
        CompleteMagicAuthSessionCommand cmd
    ) {
        var magicId = cmd.magicId();

        var session = magicLinkAuthSessionRepository
            .getSession(magicId)
            .orElseThrow(() -> new HttpBadRequestException("No magic session found."));
        

        var authType = session.authReqType();

        var claim = session.claim();

        magicLinkAuthSessionRepository.deleteSession(magicId);

        if(authType == AuthReqType.STATEFUL) {
            var sessionId = basicAuthService.createStatefulSession(claim);

            return new CompleteMagicAuthSessionResult(sessionId);
        }
        
        var token = basicAuthService.createStatelessToken(claim);


        return new CompleteMagicAuthSessionResult(token);
    }

}
