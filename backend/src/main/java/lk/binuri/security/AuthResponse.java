package lk.binuri.security;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
    private String username;
    private String token;
    private UserType role;
    private Object data;
}
