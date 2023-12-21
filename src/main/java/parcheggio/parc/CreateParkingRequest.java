package parcheggio.parc;

import lombok.Data;

@Data
public class CreateParkingRequest {
    private String citta;
    private String cap;
    private String indirizzo;
    private Integer numeroPosti;
    private String foto;
    private Integer tariffaCoperto;
    private Integer tariffaScoperto;
    private Integer tariffaSUV;
    private Integer tariffaUtilitaria;
}
