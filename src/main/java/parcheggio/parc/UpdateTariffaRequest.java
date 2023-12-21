package parcheggio.parc;

import lombok.Data;

@Data

public class UpdateTariffaRequest {
    private Integer tariffaCoperto;
    private Integer tariffaScoperto;
    private Integer tariffaSUV;
    private Integer tariffaUtilitaria;
}
