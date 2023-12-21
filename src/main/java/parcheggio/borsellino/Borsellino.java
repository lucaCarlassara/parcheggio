package parcheggio.borsellino;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import parcheggio.utenze.Utente;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Borsellino {

    @Id
    @GeneratedValue
    private Long idBorsellino;

    private Double saldo;

    @OneToOne
    @JoinColumn(name = "utente_id", unique = true)
    private Utente utente;

    public void decrementaSaldo(int importo) {
        if (saldo >= importo) {
            saldo -= importo;
        } else {
            throw new IllegalArgumentException("Saldo insufficiente nel borsellino.");
        }
    }
}
