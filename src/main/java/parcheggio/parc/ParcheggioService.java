package parcheggio.parc;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import parcheggio.utenze.Gestore;
import parcheggio.utenze.PersonaRepository;
import parcheggio.utenze.Utente;
import parcheggio.utenze.UtenteRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParcheggioService {

    private final PersonaRepository repository;

    public void createParking(String gestoreEmail, CreateParkingRequest createParkingRequest) {
        Gestore gestore = (Gestore) repository.findByEmail(gestoreEmail)
                .orElseThrow(() -> new EntityNotFoundException("Gestore non trovato"));

        Parcheggio parcheggio = Parcheggio.builder()
                .citta(createParkingRequest.getCitta())
                .cap(createParkingRequest.getCap())
                .indirizzo(createParkingRequest.getIndirizzo())
                .numeroPosti(createParkingRequest.getNumeroPosti())
                .foto(createParkingRequest.getFoto())
                .tariffaCoperto(createParkingRequest.getTariffaCoperto())
                .tariffaScoperto(createParkingRequest.getTariffaScoperto())
                .tariffaSUV(createParkingRequest.getTariffaSUV())
                .tariffaUtilitaria(createParkingRequest.getTariffaUtilitaria())
                .gestore(gestore)
                .build();

        gestore.getParcheggi().add(parcheggio);
        repository.save(gestore);
    }

    public void updateParking(String gestoreEmail, Long parcheggio_Id, UpdateNumPostiRequest updateParkingRequest) {
        Gestore gestore = (Gestore) repository.findByEmail(gestoreEmail)
                .orElseThrow(() -> new EntityNotFoundException("Gestore non trovato"));

        Parcheggio parcheggio = gestore.getParcheggi().stream()
                .filter(p -> p.getId().equals(parcheggio_Id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Parcheggio non trovato"));

        if (updateParkingRequest.getNumeroPosti() != null) {
            parcheggio.setNumeroPosti(updateParkingRequest.getNumeroPosti());
            repository.save(gestore);
        } else {
            throw new IllegalArgumentException("Il campo 'numeroPosti' deve essere fornito per la modifica.");
        }
    }

    public void updateParking(String gestoreEmail, Long parcheggio_Id, UpdateTariffaRequest updateParkingRequest) {
        Gestore gestore = (Gestore) repository.findByEmail(gestoreEmail)
                .orElseThrow(() -> new EntityNotFoundException("Gestore non trovato"));

        Parcheggio parcheggio = gestore.getParcheggi().stream()
                .filter(p -> p.getId().equals(parcheggio_Id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Parcheggio non trovato"));

        if (updateParkingRequest.getTariffaCoperto()  != null) {
            parcheggio.setTariffaCoperto(updateParkingRequest.getTariffaCoperto());
            repository.save(gestore);
        } else {
            throw new IllegalArgumentException("Il campo 'tariffaCoperto' deve essere fornito per la modifica.");
        }

        if (updateParkingRequest.getTariffaScoperto()  != null) {
            parcheggio.setTariffaScoperto(updateParkingRequest.getTariffaScoperto());
            repository.save(gestore);
        } else {
            throw new IllegalArgumentException("Il campo 'tariffaScoperto' deve essere fornito per la modifica.");
        }

        if (updateParkingRequest.getTariffaSUV()  != null) {
            parcheggio.setTariffaSUV(updateParkingRequest.getTariffaSUV());
            repository.save(gestore);
        } else {
            throw new IllegalArgumentException("Il campo 'tariffaSUV' deve essere fornito per la modifica.");
        }

        if (updateParkingRequest.getTariffaUtilitaria()  != null) {
            parcheggio.setTariffaUtilitaria(updateParkingRequest.getTariffaUtilitaria());
            repository.save(gestore);
        } else {
            throw new IllegalArgumentException("Il campo 'tariffaUtilitaria' deve essere fornito per la modifica.");
        }
    }

    public void updateParking(String gestoreEmail, Long parcheggio_Id, UpdateFotoRequest updateParkingRequest) {
        Gestore gestore = (Gestore) repository.findByEmail(gestoreEmail)
                .orElseThrow(() -> new EntityNotFoundException("Gestore non trovato"));

        Parcheggio parcheggio = gestore.getParcheggi().stream()
                .filter(p -> p.getId().equals(parcheggio_Id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Parcheggio non trovato"));

        if (updateParkingRequest.getFoto() != null) {
            parcheggio.setFoto(updateParkingRequest.getFoto());
            repository.save(gestore);
        } else {
            throw new IllegalArgumentException("Il campo 'foto' deve essere fornito per la modifica.");
        }
    }

    public void deleteParking(String gestoreEmail, Long parkingId) {
        Gestore gestore = (Gestore) repository.findByEmail(gestoreEmail)
                .orElseThrow(() -> new EntityNotFoundException("Gestore non trovato"));

        Parcheggio parcheggio = gestore.getParcheggi().stream()
                .filter(p -> p.getId().equals(parkingId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Parcheggio non trovato"));

        gestore.getParcheggi().remove(parcheggio);
        repository.save(gestore);
    }

    private final UtenteRepository utenteRepository;

    public List<UtenteDTO> getUsersForManager(String gestoreEmail) {
        Gestore gestore = (Gestore) repository.findByEmail(gestoreEmail)
                .orElseThrow(() -> new EntityNotFoundException("Gestore non trovato"));

        List<Utente> utenti = utenteRepository.findByGestoreId(gestore.getId());
        List<UtenteDTO> utentiDTO = utenti.stream()
                .map(u -> new UtenteDTO(u.getNome(), u.getCognome(), u.getEmail(), u.getTarga()))
                .collect(Collectors.toList());
        return utentiDTO;
    }


}
