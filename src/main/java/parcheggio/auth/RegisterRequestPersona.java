package parcheggio.auth;

import lombok.Data;

@Data
public class RegisterRequestPersona {

    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String citta;
    private String cap;
    private String indirizzo;

}
