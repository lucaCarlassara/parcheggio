package parcheggio.auth;

import lombok.Data;

@Data
public class RegisterRequestGestore extends RegisterRequestPersona{

    private String ragioneSociale;
    private String partitaIVA;
    private String codiceFiscale;
    private String nickname;

}
