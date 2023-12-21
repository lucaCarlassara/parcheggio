package parcheggio.prenotazione;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import parcheggio.parc.Parcheggio;
import parcheggio.utenze.TipologiaAuto;
import parcheggio.utenze.Utente;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Prenotazione {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "parcheggio_id")
    private Parcheggio parcheggio;

    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;

    @Enumerated(EnumType.STRING)
    private TipoParcheggio tipoParcheggio;

    @Enumerated(EnumType.STRING)
    private TipologiaAuto tipoAuto;

    @NotNull
    private LocalDate giorno;

    @Enumerated(EnumType.STRING)
    private FasciaOraria fasciaOraria;

    private LocalDateTime dataCreazione;

    private Integer importoAddebitato;
}


