package dev.sudhanshu.auth_n_authz.filters.session;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class SessionTokenExtractionResult {
    private String token;
    private SessionTokenType type;
}
