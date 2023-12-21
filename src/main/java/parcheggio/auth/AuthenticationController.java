package parcheggio.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register/gestore")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequestGestore request
    ){
        return ResponseEntity.ok(service.registerGestore(request));
    }

    @PostMapping("/register/utente")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequestUtente request
    ){
        return ResponseEntity.ok(service.registerUtente(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(service.authenticate(request));
    }
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestBody ChangePasswordRequest changePasswordRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        service.changePassword(userDetails.getUsername(), changePasswordRequest);
        return ResponseEntity.ok("Password cambiata con successo");
    }

}
