package dev.sudhanshu.auth_n_authz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// TODO: add logging, otp verification and 2factor.
@SpringBootApplication
public class AuthNAuthzApplication {
	public static void main(String[] args) {
		SpringApplication.run(AuthNAuthzApplication.class, args);
	}
}