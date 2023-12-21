package parcheggio.parc;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import parcheggio.utenze.Gestore;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Parcheggio {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String citta;

    @NotNull
    @Pattern(regexp = "\\d{5}", message = "Il CAP deve essere composto da 5 cifre")
    private String cap;

    @NotNull
    private String indirizzo;

    @NotNull
    private Integer numeroPosti;

    private String foto;

    private Integer tariffaCoperto;

    private Integer tariffaScoperto;

    private Integer tariffaSUV;

    private Integer tariffaUtilitaria;

    @ManyToOne
    @JoinColumn(name = "gestore_id")
    private Gestore gestore;
}
