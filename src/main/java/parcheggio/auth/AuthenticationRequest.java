package parcheggio.auth;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String email;
    String password;
}
