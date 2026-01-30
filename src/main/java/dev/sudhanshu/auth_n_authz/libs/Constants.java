package dev.sudhanshu.auth_n_authz.libs;

import java.util.concurrent.TimeUnit;

public class Constants {
    
    public static final long   COOKIE_EXPIRY_MS = TimeUnit.HOURS.toMillis(1);
    public static final String AUTHZ_HEADER_KEY = "Authorization";
    public static final String STATELESS_AUTH_COOKIE_NAME = "token";
    public static final String STATEFUL_AUTH_COOKIE_NAME = "SESSIONID";
}
