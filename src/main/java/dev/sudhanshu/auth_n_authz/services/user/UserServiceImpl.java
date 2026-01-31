package dev.sudhanshu.auth_n_authz.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.sudhanshu.auth_n_authz.repositories.user.jpa.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public boolean exists(String email) {
        var user = userRepository.findByEmail(email);

        return user.isPresent();
    }

}
