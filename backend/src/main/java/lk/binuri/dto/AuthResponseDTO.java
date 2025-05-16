package lk.binuri.dto;

import lk.binuri.security.UserType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponseDTO {
    private String username;
    private String token;
    private UserType role;
    private Object data;
}
