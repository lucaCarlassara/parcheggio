package parcheggio.parc;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UtenteDTO {

    private String nome;
    private String cognome;
    private String email;
    private String targa;

}
