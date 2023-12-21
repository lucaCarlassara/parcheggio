package parcheggio.utente;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parcheggio.utenze.PersonaRepository;
import parcheggio.utenze.Utente;

@Service
@RequiredArgsConstructor
public class UtenteService {

    @Autowired
    private final PersonaRepository repository;

    public void updateUtente(String utenteEmail, CambioTipologiaAutoRequest updateUtenteRequest) {
        Utente utente = (Utente) repository.findByEmail(utenteEmail)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));

        if (updateUtenteRequest.getTipologiaAuto() != null) {
            utente.setTipologiaAuto(updateUtenteRequest.getTipologiaAuto());
            repository.save(utente);
        } else {
            throw new IllegalArgumentException("Il campo 'tipologiaAuto' deve essere fornito per la modifica.");
        }
    }

    public void updateUtente(String utenteEmail, CambioTargaRequest updateUtenteRequest) {
        Utente utente = (Utente) repository.findByEmail(utenteEmail)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));

        if (updateUtenteRequest.getTarga() != null) {
            utente.setTarga(updateUtenteRequest.getTarga());
            repository.save(utente);
        } else {
            throw new IllegalArgumentException("Il campo 'targa' deve essere fornito per la modifica.");
        }
    }
}
