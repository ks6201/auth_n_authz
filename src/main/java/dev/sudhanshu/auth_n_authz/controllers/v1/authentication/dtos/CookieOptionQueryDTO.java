package dev.sudhanshu.auth_n_authz.controllers.v1.authentication.dtos;

import dev.sudhanshu.auth_n_authz.libs.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CookieOptionQueryDTO {
    private Boolean cookie = false;
    private Boolean httpOnly = true;
    private Boolean secure = true;
    private String path = "/";
    private Long maxAge = Constants.COOKIE_EXPIRY_MS;
    private String sameSite = "Strict";
}
