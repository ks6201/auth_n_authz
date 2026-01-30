package dev.sudhanshu.auth_n_authz.libs.payload;

import lombok.Data;

import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class APIResponse<T> {
    public T payload;
    private boolean isError;

    public static <T> APIResponse<T> error(T payload) {
        return new APIResponse<>(payload, true);
    }

    public static <T> APIResponse<T> success(T payload) {
        return new APIResponse<>(payload, false);
    }
}
