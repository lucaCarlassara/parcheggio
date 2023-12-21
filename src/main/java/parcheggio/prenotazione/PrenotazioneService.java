package parcheggio.prenotazione;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import parcheggio.borsellino.BorsellinoRepository;
import parcheggio.borsellino.BorsellinoService;
import parcheggio.parc.ParcheggioRepository;
import parcheggio.utenze.*;
import parcheggio.parc.Parcheggio;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class PrenotazioneService {

    private final PersonaRepository personaRepository;
    private final ParcheggioRepository parcheggioRepository;
    private final PrenotazioneRepository prenotazioneRepository;
    private final BorsellinoRepository borsellinoRepository;

    public void createReservation(String userEmail, Long parcheggioId, TipoParcheggio tipoParcheggio, TipologiaAuto tipoAuto, LocalDate giorno, FasciaOraria fasciaOraria) {
        Utente utente = (Utente) personaRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));

        Parcheggio parcheggio = parcheggioRepository.findById(parcheggioId)
                .orElseThrow(() -> new EntityNotFoundException("Parcheggio non trovato"));

        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setUtente(utente);
        prenotazione.setParcheggio(parcheggio);
        prenotazione.setTipoParcheggio(tipoParcheggio);
        prenotazione.setTipoAuto(tipoAuto);
        prenotazione.setGiorno(giorno);
        prenotazione.setFasciaOraria(fasciaOraria);
        prenotazione.setDataCreazione(LocalDateTime.now());

        int importoTotale = calcolaImportoTotale(prenotazione.getTipoParcheggio(), prenotazione.getTipoAuto(), parcheggio);

        if (utente.getBorsellino().getSaldo() >= importoTotale) {
            utente.getBorsellino().decrementaSaldo(importoTotale);
            borsellinoRepository.save(utente.getBorsellino());
            prenotazioneRepository.save(prenotazione);
            try {
                String filePath = "src/main/resources/RicevutePrenotazioni/prenotazione_" + prenotazione.getId() + ".pdf";
                generaPdfPrenotazione(filePath, prenotazione);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("Saldo insufficiente nel borsellino.");
        }

    }

    public PrenotazioneDto getDettagliPrenotazione(Long prenotazioneId, String gestoreEmail) throws AccessDeniedException {
        Prenotazione prenotazione = prenotazioneRepository.findById(prenotazioneId)
                .orElseThrow(() -> new EntityNotFoundException("Prenotazione non trovata con ID: " + prenotazioneId));

        Utente utente = prenotazione.getUtente();

        Gestore gestore = (Gestore) personaRepository.findByEmail(gestoreEmail)
                .orElseThrow(() -> new EntityNotFoundException("Gestore non trovato con email: " + gestoreEmail));

        if (!gestore.getParcheggi().contains(prenotazione.getParcheggio())) {
            throw new AccessDeniedException("L'utente autenticato non ha accesso ai dettagli della prenotazione.");
        }

        return PrenotazioneDto.builder()
                .nomeUtente(utente.getNome())
                .cognomeUtente(utente.getCognome())
                .emailUtente(utente.getEmail())
                .fasciaOraria(prenotazione.getFasciaOraria())
                .idParcheggio(prenotazione.getParcheggio().getId())
                .build();
    }

    public void aggiornaPrenotazione(Long prenotazioneId, AggiornaPrenotazioneRequest aggiornaPrenotazioneRequest) {
        Prenotazione prenotazione = prenotazioneRepository.findById(prenotazioneId)
                .orElseThrow(() -> new EntityNotFoundException("Prenotazione non trovata"));

        LocalDateTime oraAttuale = LocalDateTime.now();
        LocalDateTime oraCreazionePrenotazione = prenotazione.getDataCreazione();
        long oreTrascorse = ChronoUnit.HOURS.between(oraCreazionePrenotazione, oraAttuale);

        if (oreTrascorse > 24) {
            throw new IllegalArgumentException("Non è possibile modificare la prenotazione dopo 24 ore dalla creazione.");
        }

        String filePathPrenotazionePrecedente = "src/main/resources/RicevutePrenotazioni/prenotazione_" + prenotazione.getId() + ".pdf";

        try {
            Files.deleteIfExists(Paths.get(filePathPrenotazionePrecedente));
        } catch (IOException e) {
            e.printStackTrace();
        }

        prenotazione.setGiorno(aggiornaPrenotazioneRequest.getNuovoGiorno());
        prenotazione.setFasciaOraria(aggiornaPrenotazioneRequest.getNuovaFasciaOraria());

        Prenotazione prenotazioneAggiornata = prenotazioneRepository.save(prenotazione);

        try {
            String filePathNuovoPdf = "src/main/resources/RicevutePrenotazioni/prenotazione_" + prenotazioneAggiornata.getId() + ".pdf";
            generaPdfPrenotazione(filePathNuovoPdf, prenotazioneAggiornata);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final BorsellinoService borsellinoService;

    public void cancellaPrenotazione(Long prenotazioneId) {
        Prenotazione prenotazione = prenotazioneRepository.findById(prenotazioneId)
                .orElseThrow(() -> new EntityNotFoundException("Prenotazione non trovata"));
        Parcheggio parcheggio = parcheggioRepository.findById(prenotazione.getParcheggio().getId())
                .orElseThrow(() -> new EntityNotFoundException("Parcheggio non trovato"));

        LocalDateTime oraAttuale = LocalDateTime.now();
        LocalDateTime oraCreazionePrenotazione = prenotazione.getDataCreazione();
        long oreTrascorse = ChronoUnit.HOURS.between(oraCreazionePrenotazione, oraAttuale);

        if (oreTrascorse > 24) {
            throw new IllegalArgumentException("Non è possibile cancellare la prenotazione dopo 24 ore dalla creazione.");
        }

        Integer importoAddebitato = calcolaImportoTotale(prenotazione.getTipoParcheggio(), prenotazione.getTipoAuto(), parcheggio);

        borsellinoService.rimborso(prenotazione.getUtente(), importoAddebitato);

        prenotazioneRepository.delete(prenotazione);
    }

    public void generaPdfPrenotazione(String filePath, Prenotazione prenotazione) throws IOException {
        try (PdfWriter writer = new PdfWriter(filePath);
             PdfDocument pdfDocument = new PdfDocument(writer);
             Document document = new Document(pdfDocument)) {

            document.add(new Paragraph("Conferma Prenotazione"));
            document.add(new Paragraph("Nome: " + prenotazione.getUtente().getNome()));
            document.add(new Paragraph("Cognome: " + prenotazione.getUtente().getCognome()));
            document.add(new Paragraph("ID Prenotazione: " + prenotazione.getId()));
            document.add(new Paragraph("ID Parcheggio: " + prenotazione.getParcheggio().getId()));
            document.add(new Paragraph("Tipo Parcheggio: " + prenotazione.getTipoParcheggio()));
            document.add(new Paragraph("Tipo Auto: " + prenotazione.getTipoAuto()));
            document.add(new Paragraph("Giorno: " + prenotazione.getGiorno()));
            document.add(new Paragraph("Fascia Oraria: " + prenotazione.getFasciaOraria()));
            document.add(new Paragraph("Importo Totale €: " + calcolaImportoTotale(prenotazione.getTipoParcheggio(),
                    prenotazione.getTipoAuto(), prenotazione.getParcheggio())));
        }
    }

    private int calcolaImportoTotale(TipoParcheggio tipoParcheggio, TipologiaAuto tipoAuto, Parcheggio parcheggio) {
        int importoAggiuntivo = 0;

        switch (tipoParcheggio) {
            case COPERTO:
                importoAggiuntivo = parcheggio.getTariffaCoperto();
                break;
            case SCOPERTO:
                importoAggiuntivo = parcheggio.getTariffaScoperto();
                break;

        }

        switch (tipoAuto) {
            case SUV:
                importoAggiuntivo += parcheggio.getTariffaSUV();
                break;
            case Utilitaria:
                importoAggiuntivo += parcheggio.getTariffaUtilitaria();
                break;
        }

        return importoAggiuntivo;
    }
}
