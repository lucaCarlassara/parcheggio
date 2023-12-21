package parcheggio.prenotazione;

import lombok.Data;
import parcheggio.utenze.TipologiaAuto;
import java.time.LocalDate;

@Data
public class PrenotazioneRequest {
    private Long parcheggioId;
    private TipoParcheggio tipoParcheggio;
    private TipologiaAuto tipologiaAuto;
    private LocalDate giorno;
    private FasciaOraria fasciaOraria;
}
