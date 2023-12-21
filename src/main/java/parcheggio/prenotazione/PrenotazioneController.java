package parcheggio.prenotazione;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import parcheggio.parc.ParcheggioService;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/prenotazione")
@RequiredArgsConstructor
public class PrenotazioneController {

    private final PrenotazioneService prenotazioneService;

    @PostMapping("/create-reservation")
    public ResponseEntity<String> createReservation(
            @RequestBody PrenotazioneRequest prenotazioneRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        prenotazioneService.createReservation(
                userDetails.getUsername(),
                prenotazioneRequest.getParcheggioId(),
                prenotazioneRequest.getTipoParcheggio(),
                prenotazioneRequest.getTipologiaAuto(),
                prenotazioneRequest.getGiorno(),
                prenotazioneRequest.getFasciaOraria()
        );
        return ResponseEntity.ok("Pagamento e prenotazione effettuati con successo");
    }

    @PostMapping("/aggiorna-prenotazione/{prenotazioneId}")
    public ResponseEntity<String> aggiornaPrenotazione(
            @PathVariable Long prenotazioneId,
            @RequestBody AggiornaPrenotazioneRequest aggiornaPrenotazioneRequest
    ) {
        prenotazioneService.aggiornaPrenotazione(prenotazioneId, aggiornaPrenotazioneRequest);
        return ResponseEntity.ok("Prenotazione aggiornata con successo");
    }

    @DeleteMapping("/cancella-prenotazione/{prenotazioneId}")
    public ResponseEntity<String> cancellaPrenotazione(
            @PathVariable Long prenotazioneId
    ) {
        prenotazioneService.cancellaPrenotazione(prenotazioneId);
        return ResponseEntity.ok("Prenotazione cancellata con successo");
    }

    @GetMapping("/dettagli/{prenotazioneId}")
    public ResponseEntity<PrenotazioneDto> getDettagliPrenotazione(
            @PathVariable Long prenotazioneId,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws AccessDeniedException {
        PrenotazioneDto dettagliPrenotazione = prenotazioneService.getDettagliPrenotazione(prenotazioneId, userDetails.getUsername());
        return ResponseEntity.ok(dettagliPrenotazione);
    }
}
