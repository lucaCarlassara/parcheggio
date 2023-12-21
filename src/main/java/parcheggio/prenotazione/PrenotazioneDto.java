package parcheggio.prenotazione;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PrenotazioneDto {
    private String nomeUtente;
    private String cognomeUtente;
    private String emailUtente;
    private FasciaOraria fasciaOraria;
    private Long idParcheggio;
}
