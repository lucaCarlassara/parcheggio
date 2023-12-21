package parcheggio.borsellino;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import parcheggio.utenze.PersonaRepository;
import parcheggio.utenze.Utente;

@Service
@RequiredArgsConstructor
public class BorsellinoService {

    private final PersonaRepository personaRepository;
    private final BorsellinoRepository borsellinoRepository;

    public Double getSaldo(String userEmail) {
        Utente utente = (Utente) personaRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));

        Borsellino borsellino = utente.getBorsellino();
        return (borsellino != null) ? borsellino.getSaldo() : 0.0;
    }

    public void caricaBorsellino(String userEmail, Double amount) {
        Utente utente = (Utente) personaRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));

        Borsellino borsellino = utente.getBorsellino();
        if (borsellino != null) {
            double currentBalance = borsellino.getSaldo();
            borsellino.setSaldo(currentBalance + amount);
            personaRepository.save(utente);
        } else {
            throw new IllegalStateException("Il borsellino non è stato creato per l'utente");
        }
    }

    public void rimborso(Utente utente, Integer importo) {
        Borsellino borsellino = utente.getBorsellino();

        if (borsellino == null) {
            throw new EntityNotFoundException("Borsellino non trovato per l'utente.");
        }

        borsellino.setSaldo(borsellino.getSaldo() + importo);

        borsellinoRepository.save(borsellino);
    }

}
