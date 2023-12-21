package parcheggio.auth;

import lombok.Data;
import parcheggio.utenze.Gestore;
import parcheggio.utenze.TipologiaAuto;

@Data
public class RegisterRequestUtente extends RegisterRequestPersona {

    private String targa;
    private TipologiaAuto tipologiaAuto;
    private Gestore gestore;

}
