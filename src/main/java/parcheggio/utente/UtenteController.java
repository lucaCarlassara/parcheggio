package parcheggio.utente;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UtenteController {

    private final UtenteService service;

    @PostMapping("/update-tipologiaAuto/{utente_id}")
    public ResponseEntity<String> updateUtente(
            @RequestBody CambioTipologiaAutoRequest updateUtenteRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        service.updateUtente(userDetails.getUsername(), updateUtenteRequest);
        return ResponseEntity.ok("Tipologia Auto aggiornata con successo");
    }

    @PostMapping("/update-targa/{utente_id}")
    public ResponseEntity<String> updateUtente(
            @RequestBody CambioTargaRequest updateUtenteRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        service.updateUtente(userDetails.getUsername(), updateUtenteRequest);
        return ResponseEntity.ok("Targa aggiornata con successo");
    }
}
