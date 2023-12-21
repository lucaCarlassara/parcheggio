package parcheggio.prenotazione;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AggiornaPrenotazioneRequest {
    private LocalDate nuovoGiorno;
    private FasciaOraria nuovaFasciaOraria;
}
