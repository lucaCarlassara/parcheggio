package parcheggio.parc;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class ParcheggioController {

    private final ParcheggioService service;

    @PostMapping("/crea-parcheggio")
    public ResponseEntity<String> createParking(
            @RequestBody CreateParkingRequest createParkingRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        service.createParking(userDetails.getUsername(), createParkingRequest);
        return ResponseEntity.ok("Parcheggio creato con successo");
    }

    @PostMapping("/update-numPosti/{parcheggio_id}")
    public ResponseEntity<String> updateParking(
            @PathVariable Long parcheggio_id,
            @RequestBody UpdateNumPostiRequest updateParkingRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        service.updateParking(userDetails.getUsername(), parcheggio_id, updateParkingRequest);
        return ResponseEntity.ok("Parcheggio aggiornato con successo");
    }

    @PostMapping("/update-tariffa/{parcheggio_id}")
    public ResponseEntity<String> updateParking(
            @PathVariable Long parcheggio_id,
            @RequestBody UpdateTariffaRequest updateParkingRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        service.updateParking(userDetails.getUsername(), parcheggio_id, updateParkingRequest);
        return ResponseEntity.ok("Parcheggio aggiornato con successo");
    }

    @PostMapping("/update-foto/{parcheggio_id}")
    public ResponseEntity<String> updateParking(
            @PathVariable Long parcheggio_id,
            @RequestBody UpdateFotoRequest updateParkingRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        service.updateParking(userDetails.getUsername(), parcheggio_id, updateParkingRequest);
        return ResponseEntity.ok("Parcheggio aggiornato con successo");
    }

    @DeleteMapping("/delete-parking/{parkingId}")
    public ResponseEntity<String> deleteParking(
            @PathVariable Long parkingId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        service.deleteParking(userDetails.getUsername(), parkingId);
        return ResponseEntity.ok("Parcheggio cancellato con successo");
    }

    @GetMapping("/utenti-per-gestore")
    public ResponseEntity<List<UtenteDTO>> getUsersForManager(@AuthenticationPrincipal UserDetails userDetails) {
        List<UtenteDTO> utenti = service.getUsersForManager(userDetails.getUsername());
        return ResponseEntity.ok(utenti);
    }

}
