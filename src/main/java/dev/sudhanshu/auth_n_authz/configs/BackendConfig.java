package dev.sudhanshu.auth_n_authz.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BackendConfig {

    @Value("${backend.protocol}")
    private String protocol;

    @Value("${backend.domain}")
    private String domain;

    public String getProtocol() {
        return protocol;
    }

    public String getDomain() {
        return domain;
    }

    public String getBackendUri() {
        return String.format("%s://%s", protocol, domain);
    }
}
