package parcheggio.utente;

import lombok.Data;
import parcheggio.utenze.TipologiaAuto;

@Data
public class CambioTipologiaAutoRequest {
    private TipologiaAuto tipologiaAuto;
}
