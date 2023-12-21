package parcheggio.auth;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import parcheggio.borsellino.BorsellinoRepository;
import parcheggio.config.JwtService;
import parcheggio.utenze.*;
import parcheggio.borsellino.Borsellino;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PersonaRepository repository;
    private final BorsellinoRepository borsellinoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse registerGestore(RegisterRequestGestore request) {

        var gestore = Gestore.builder()
                .nome(request.getNome())
                .cognome(request.getCognome())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .citta(request.getCitta())
                .cap(request.getCap())
                .indirizzo(request.getIndirizzo())
                .role(Role.GESTORE)
                .ragioneSociale(request.getRagioneSociale())
                .partitaIVA(request.getPartitaIVA())
                .codiceFiscale(request.getCodiceFiscale())
                .nickname(request.getNickname())
                .build();
        repository.save(gestore);  // Salva l'istanza di Gestore nel database

        var jwtToken = jwtService.generateToken(gestore);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse registerUtente(RegisterRequestUtente request) {

        var utente = Utente.builder()
                .nome(request.getNome())
                .cognome(request.getCognome())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .citta(request.getCitta())
                .cap(request.getCap())
                .indirizzo(request.getIndirizzo())
                .role(Role.UTENTE)
                .targa(request.getTarga())
                .tipologiaAuto(request.getTipologiaAuto())
                .gestore(request.getGestore())
                .build();

        repository.save(utente);

        Borsellino borsellino = Borsellino.builder()
                .saldo(0.0)
                .utente(utente)
                .build();

        borsellinoRepository.save(borsellino);
        utente.setBorsellino(borsellino);

        var jwtToken = jwtService.generateToken(utente);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public void changePassword(String email, ChangePasswordRequest request) {
        Persona user = repository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));

        // Verifica che la vecchia password fornita coincida con la password attuale
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BadCredentialsException("La vecchia password non è corretta");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        repository.save(user);
    }
}
