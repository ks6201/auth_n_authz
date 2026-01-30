package dev.sudhanshu.auth_n_authz.services.session.validation;


import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SessionValidationServiceImplTests {

    // @Mock
    // private SessionRepository sessionRepository;

    // @Mock
    // private JwtProvider jwtProvider;

    // @InjectMocks
    // SessionValidationServiceImpl service;

    // @Test
    // void validateStatefulSession_success() {
    //     var cmd = new SessionValidateCommand("session-token");

    //     when(sessionRepository.exists("session-token"))
    //         .thenReturn(true);

    //     this.service.validateStatefulSession(cmd);

    //     verify(sessionRepository)
    //         .exists("session-token");
    // }

    // @Test
    // void validateStatefulSession_fails_whenTokenIsBad() {
    //     var cmd = new SessionValidateCommand("bad-token");       

    //     when(sessionRepository.exists("bad-token"))
    //         .thenReturn(false);
        
    //     assertThrows(SessionValidationFailedException.class, () -> {
    //         this.service.validateStatefulSession(cmd);
    //     });

    //     verify(sessionRepository)
    //         .exists("bad-token");
    // }

    // @Test
    // void validateStatelessSession_success() {
    //     var cmd = new SessionValidateCommand("session-token");

    //     when(jwtProvider.verify("session-token"))
    //         .thenReturn("mail@mail.co");

    //     var claim = this.service.validateStatelessSession(cmd);

    //     assertEquals("mail@mail.co", claim);

    //     verify(jwtProvider)
    //         .verify("session-token");
    // }

    // @Test
    // void validateStatelessSession_fails_whenTokenIsExpired() {
    //     var cmd = new SessionValidateCommand("expired-token");

    //     when(jwtProvider.verify("expired-token"))
    //         .thenThrow(
    //             new ExpiredJwtException(null, null, "JWT expired")
    //         );

    //     assertThrows(TokenExpiredException.class, () -> {
    //         this.service.validateStatelessSession(cmd);
    //     });
    // }

    // @Test
    // void validateStatelessSession_fails_whenTokenIsMalformed() {
    //     var cmd = new SessionValidateCommand("malformed-token");

    //     when(jwtProvider.verify("malformed-token"))
    //         .thenThrow(
    //             new MalformedJwtException("malformed token")
    //         );

    //     assertThrows(InvalidTokenException.class, () -> {
    //         this.service.validateStatelessSession(cmd);
    //     });
    // }

    // @Test
    // void validateStatelessSession_fails_whenItThrowsIllegalArgExceptionOnBadToken() {
    //     var cmd = new SessionValidateCommand("bad-token");

    //     when(jwtProvider.verify("bad-token"))
    //         .thenThrow(
    //             new IllegalArgumentException("bad token")
    //         );

    //     assertThrows(InvalidTokenException.class, () -> {
    //         this.service.validateStatelessSession(cmd);
    //     });
    // }

    // @Test
    // void validateStatelessSession_fails_whenItThrowsAnyException() {
    //     var cmd = new SessionValidateCommand("bad-token");

    //     when(jwtProvider.verify("bad-token"))
    //         .thenThrow(
    //             new RuntimeException("bad token")
    //         );

    //     assertThrows(APIException.class, () -> {
    //         this.service.validateStatelessSession(cmd);
    //     });
    // }
}
