package parcheggio.borsellino;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/utente")
@RequiredArgsConstructor
public class BorsellinoController {

    private final BorsellinoService service;

    @GetMapping("/saldo")
    public ResponseEntity<Double> getWalletBalance(@AuthenticationPrincipal UserDetails userDetails) {
        Double balance = service.getSaldo(userDetails.getUsername());
        return ResponseEntity.ok(balance);
    }

    @PostMapping("/carica-saldo")
    public ResponseEntity<String> loadWallet(
            @RequestBody CaricaBorsellinoRequest caricaBorsellinoRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        service.caricaBorsellino(userDetails.getUsername(), caricaBorsellinoRequest.getAmount());
        return ResponseEntity.ok("Borsellino caricato con successo");
    }

}
