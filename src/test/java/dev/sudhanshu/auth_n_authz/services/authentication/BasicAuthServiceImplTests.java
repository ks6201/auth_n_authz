package dev.sudhanshu.auth_n_authz.services.authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import dev.sudhanshu.auth_n_authz.entities.Application;
import dev.sudhanshu.auth_n_authz.entities.User;
import dev.sudhanshu.auth_n_authz.libs.security.hasher.password.PasswordHasher;
import dev.sudhanshu.auth_n_authz.libs.security.jwt.JwtProvider;
import dev.sudhanshu.auth_n_authz.repositories.application.jpa.ApplicationRepository;
import dev.sudhanshu.auth_n_authz.repositories.blacklist.BlacklistRepository;
import dev.sudhanshu.auth_n_authz.repositories.session.SessionRepository;
import dev.sudhanshu.auth_n_authz.repositories.user.jpa.UserRepository;
import dev.sudhanshu.auth_n_authz.services.authentication.basic.BasicAuthServiceImpl;
import dev.sudhanshu.auth_n_authz.services.authentication.basic.commands.CreateUserCommand;
import dev.sudhanshu.auth_n_authz.services.authentication.basic.commands.LoginCommand;
import dev.sudhanshu.auth_n_authz.services.authentication.basic.commands.LogoutCommand;
import dev.sudhanshu.auth_n_authz.services.authentication.basic.exceptions.InvalidCredentialsException;
import dev.sudhanshu.auth_n_authz.services.authentication.basic.exceptions.LogoutFailedException;
import dev.sudhanshu.auth_n_authz.services.authentication.basic.exceptions.UserAlreadyExistsException;
import dev.sudhanshu.auth_n_authz.services.authentication.basic.exceptions.UserAlreadyLoggedOutException;
import dev.sudhanshu.auth_n_authz.services.authentication.basic.exceptions.UserNotFoundException;
import dev.sudhanshu.auth_n_authz.services.authentication.basic.results.StatefulLoginResult;
import dev.sudhanshu.auth_n_authz.services.authentication.basic.results.StatelessLoginResult;
import dev.sudhanshu.auth_n_authz.services.session.validation.stateful.StatefulSessionValidationService;
import dev.sudhanshu.auth_n_authz.services.session.validation.stateless.StatelessSessionValidationService;

// GIVEN  (define world)
// WHEN   (execute behavior)
// THEN   (assert outcome)
// AND    (verify interactions)

@ExtendWith(MockitoExtension.class)
public class BasicAuthServiceImplTests {

    @Mock
    UserRepository userRepository;

    @Mock
    SessionRepository sessionRepository;

    @Mock
    PasswordHasher passwordHasher;

    @Mock
    BlacklistRepository blacklistRepository;

    @Mock
    JwtProvider jwtProvider;

    @Mock
    ApplicationRepository applicationRepository;

    @Mock
    StatelessSessionValidationService statelessSessionValidationService;
    
    @Mock
    StatefulSessionValidationService statefulSessionValidationService;


    @InjectMocks
    BasicAuthServiceImpl service;


    @Test
    void createUser_success() {
        var command = new CreateUserCommand(
            "mail@mail.co",
            "passwd",
            "uniqueApp"
        );

        when(passwordHasher.hash("passwd"))
            .thenReturn("hashedPasswd");

            
        Application newApp = new Application();
        newApp.setApplicationName("uniqueApp");

        when(applicationRepository.findByApplicationName("uniqueApp"))
            .thenReturn(Optional.empty());

        var savedAppUUID = UUID.randomUUID();
        var savedApp = new Application();
        savedApp.setApplicationId(savedAppUUID);
        savedApp.setApplicationName(newApp.getApplicationName());

        when(applicationRepository.save(any(Application.class)))
            .thenReturn(savedApp);


        User user = new User();
        user.setEmail(command.email());
        user.setPassword("hashedPasswd");
        user.setApplication(newApp);
        

        var savedUserUUID = UUID.randomUUID();
        var savedUser = new User(
            savedUserUUID,
            user.getEmail(),
            user.getPassword(),
            user.getApplication()
        );

        when(userRepository.save(any(User.class)))
            .thenReturn(savedUser);

        var createdUserResult = this.service.createUser(command);

        assertEquals(createdUserResult.userId(), savedUserUUID);
        assertEquals(createdUserResult.email(), user.getEmail());

        verify(passwordHasher)
            .hash("passwd");

        verify(applicationRepository)
            .findByApplicationName("uniqueApp");


        verify(applicationRepository)
            .save(any(Application.class));

        verify(userRepository)
            .save(any(User.class));

        var order = inOrder(
            passwordHasher,
            applicationRepository,
            userRepository
        );

        order.verify(passwordHasher).hash("passwd");

        order.verify(applicationRepository)
            .findByApplicationName("uniqueApp");

        order.verify(applicationRepository)
            .save(any(Application.class));
            
        order.verify(userRepository)
            .save(any(User.class));

        verifyNoMoreInteractions(
            passwordHasher,
            applicationRepository,
            userRepository
        );

    }

    @Test
    void createUser_success_whenApplicationNameExists() {
        var command = new CreateUserCommand(
            "mail@mail.co",
            "passwd",
            "uniqueApp"
        );

        when(passwordHasher.hash("passwd"))
            .thenReturn("hashedPasswd");

        var existingAppUUID = UUID.randomUUID();
        var existingApp = new Application();
        existingApp.setApplicationId(existingAppUUID);
        existingApp.setApplicationName("uniqueApp");

        when(applicationRepository.findByApplicationName("uniqueApp"))
            .thenReturn(Optional.of(existingApp));

        var savedUserUUID = UUID.randomUUID();
        var savedUser = new User(
            savedUserUUID,
            command.email(),
            "hashedPasswd",
            existingApp
        );

        when(userRepository.save(any(User.class)))
            .thenReturn(savedUser);

        var result = service.createUser(command);

        assertEquals(savedUserUUID, result.userId());
        assertEquals(command.email(), result.email());

        var order = inOrder(
            passwordHasher,
            applicationRepository,
            userRepository
        );

        order.verify(passwordHasher).hash("passwd");

        order.verify(applicationRepository)
            .findByApplicationName("uniqueApp");

        order.verify(userRepository)
            .save(any(User.class));

        verify(applicationRepository, never())
            .save(any(Application.class));

        verifyNoMoreInteractions(
            passwordHasher,
            applicationRepository,
            userRepository
        );
    }


    @Test
    void createUser_fails_whenEmailAlreadyExists() {

        var command = new CreateUserCommand(
            "mail@mail.co",
            "passwd",
            "uniqueApp"
        );

        when(passwordHasher.hash("passwd"))
            .thenReturn("hashedPasswd");

            
        Application newApp = new Application();
        newApp.setApplicationName("uniqueApp");

        when(applicationRepository.findByApplicationName("uniqueApp"))
            .thenReturn(Optional.empty());

        var savedAppUUID = UUID.randomUUID();
        var savedApp = new Application();
        savedApp.setApplicationId(savedAppUUID);
        savedApp.setApplicationName(newApp.getApplicationName());

        when(applicationRepository.save(any(Application.class)))
            .thenReturn(savedApp);


        User user = new User();
        user.setEmail(command.email());
        user.setPassword("hashedPasswd");
        user.setApplication(newApp);

        doThrow(new DataIntegrityViolationException("constraint violation"))
            .when(userRepository)
            .save(any(User.class));

        assertThrows(UserAlreadyExistsException.class, () -> {
            this.service.createUser(command);
        });

        verify(passwordHasher)
            .hash("passwd");

        verify(applicationRepository)
            .findByApplicationName("uniqueApp");


        verify(applicationRepository)
            .save(any(Application.class));

        verify(userRepository)
            .save(any(User.class));

        var order = inOrder(
            passwordHasher,
            applicationRepository,
            userRepository
        );

        order.verify(passwordHasher).hash("passwd");

        order.verify(applicationRepository)
            .findByApplicationName("uniqueApp");

        order.verify(applicationRepository)
            .save(any(Application.class));

        order.verify(userRepository)
            .save(any(User.class));

        verifyNoMoreInteractions(
            passwordHasher,
            applicationRepository,
            userRepository
        );

    }


    @Test
    void statelessAuthentication_success() {
        User user = new User();
        user.setUserId(UUID.randomUUID());
        user.setEmail("mail@mail.co");
        user.setPassword("hashedPassword");

        LoginCommand loginCommand = new LoginCommand(
            "mail@mail.co",
            "passwd"
        );

        when(userRepository.findByEmail("mail@mail.co"))
            .thenReturn(Optional.of(user));

        when(passwordHasher.verify("passwd", "hashedPassword"))
            .thenReturn(true);

        when(jwtProvider.sign(any()))
            .thenReturn("fake-jwt-token");

        StatelessLoginResult result = 
            service.statelessAuthenticate(loginCommand);

        assertNotNull(result.jwt());
        assertFalse(result.jwt().isBlank());
        assertEquals("fake-jwt-token", result.jwt());

        verify(jwtProvider).sign(any());
        verifyNoInteractions(sessionRepository);
        verify(userRepository).findByEmail("mail@mail.co");
        verify(passwordHasher).verify("passwd", "hashedPassword");
    }

    @Test
    void statelessAuthentication_fails_whenUserNotFound() {
        
        LoginCommand loginCommand = new LoginCommand(
            "mail@mail.co",
            "passwd"
        );

        when(userRepository.findByEmail("mail@mail.co"))
            .thenReturn(Optional.empty());

        
        assertThrows(UserNotFoundException.class, () ->
            service.statelessAuthenticate(loginCommand)
        );
    }

    @Test
    void statelessAuthentication_fails_whenPasswordIsInvalid() {
        
        User user = new User();
        user.setUserId(UUID.randomUUID());
        user.setEmail("mail@mail.co");
        user.setPassword("hashedPassword");

        LoginCommand loginCommand = new LoginCommand(
            "mail@mail.co",
            "passwd"
        );

        when(userRepository.findByEmail("mail@mail.co"))
            .thenReturn(Optional.of(user));

        when(passwordHasher.verify("passwd", "hashedPassword"))
            .thenReturn(false);

        
        assertThrows(InvalidCredentialsException.class, () ->
            service.statelessAuthenticate(loginCommand)
        );
    }

    @Test
    public void statefulAuthentication_success() {
        User user = new User();
        user.setUserId(UUID.randomUUID());
        user.setEmail("mail@mail.co");
        user.setPassword("hashedPassword");

        LoginCommand loginCommand = new LoginCommand(
            "mail@mail.co",
            "passwd"
        );

        when(userRepository.findByEmail("mail@mail.co"))
            .thenReturn(Optional.of(user));

        when(passwordHasher.verify("passwd", "hashedPassword"))
            .thenReturn(true);

        StatefulLoginResult result = 
            service.statefulAuthenticate(loginCommand);

        assertNotNull(result.sessionId());
        assertFalse(result.sessionId().isBlank());

        verify(userRepository).findByEmail("mail@mail.co");
        verify(passwordHasher).verify("passwd", "hashedPassword");
    }

    @Test
    public void statelessLogout_success() {

        var logoutCommand = new LogoutCommand("session-token");

        when(blacklistRepository.isBlacklisted("session-token"))
            .thenReturn(false);
    
        when(blacklistRepository.blacklist("session-token"))
            .thenReturn(true);
        
        this.service.statelessLogout(logoutCommand);

        verify(blacklistRepository)
            .blacklist("session-token");

        InOrder inOrder = inOrder(blacklistRepository);

        inOrder.verify(blacklistRepository)
            .blacklist("session-token");

        verifyNoMoreInteractions(blacklistRepository);
    }

    @Test
    public void statelessLogout_fails_whenAlreadyBlacklisted() {

        var logoutCommand = new LogoutCommand("bad-token");

        when(blacklistRepository.isBlacklisted("bad-token"))
            .thenReturn(false);
    
        when(blacklistRepository.blacklist("bad-token"))
            .thenReturn(false);

        assertThrows(LogoutFailedException.class, () -> {
            this.service.statelessLogout(logoutCommand);
        });

        verify(blacklistRepository)
            .isBlacklisted("bad-token");

        verify(blacklistRepository)
            .blacklist("bad-token");
    }

    @Test
    public void statelessLogout_fails_whenBlacklistingFails() {

        var logoutCommand = new LogoutCommand("bad-token");

        when(blacklistRepository.isBlacklisted("bad-token"))
            .thenReturn(true);

        assertThrows(UserAlreadyLoggedOutException.class, () -> {
            this.service.statelessLogout(logoutCommand);
        });
    
        verify(blacklistRepository)
            .isBlacklisted("bad-token");
    }

    @Test
    public void statefulLogout_success() {

        var logoutCommand = new LogoutCommand("session-token");

        when(sessionRepository.deleteSession("session-token"))
            .thenReturn(true);
        
        this.service.statefulLogout(logoutCommand);

        verify(sessionRepository)
            .deleteSession("session-token");
    }

    @Test
    public void statefulfulLogout_fails_whenTokenIsBad() {

        var logoutCommand = new LogoutCommand("bad-token");

        when(sessionRepository.deleteSession("bad-token"))
            .thenReturn(false);
        
        assertThrows(LogoutFailedException.class, () -> {
            this.service.statefulLogout(logoutCommand);
        });

        verify(sessionRepository)
            .deleteSession("bad-token");
    }
}

