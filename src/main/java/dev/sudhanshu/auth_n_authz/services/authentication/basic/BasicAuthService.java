package dev.sudhanshu.auth_n_authz.services.authentication.basic;

import dev.sudhanshu.auth_n_authz.services.authentication.basic.commands.CreateUserCommand;
import dev.sudhanshu.auth_n_authz.services.authentication.basic.commands.LoginCommand;
import dev.sudhanshu.auth_n_authz.services.authentication.basic.commands.LogoutCommand;
import dev.sudhanshu.auth_n_authz.services.authentication.basic.results.CreateUserResult;
import dev.sudhanshu.auth_n_authz.services.authentication.basic.results.StatefulLoginResult;
import dev.sudhanshu.auth_n_authz.services.authentication.basic.results.StatelessLoginResult;

public interface BasicAuthService {
    StatelessLoginResult statelessAuthenticate(LoginCommand loginCommand);
    StatefulLoginResult statefulAuthenticate(LoginCommand loginCommand);
    void statelessLogout(LogoutCommand logoutCommand);
    void statefulLogout(LogoutCommand logoutCommand);
    CreateUserResult createUser(CreateUserCommand createUserCommand);
}
