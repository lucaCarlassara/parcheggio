package parcheggio.utenze;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;
import parcheggio.borsellino.Borsellino;
import parcheggio.prenotazione.Prenotazione;

import java.util.List;

@Data
@SuperBuilder(toBuilder = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "utente_id")
public class Utente extends Persona{

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @NotNull
    @Pattern(regexp = "[A-Z]{2}\\d{3}[A-Z]{2}", message = "Formato targa non valido")
    private String targa;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TipologiaAuto tipologiaAuto;

    @ManyToOne
    @JoinColumn(name = "gestore_id")
    private Gestore gestore;

    @OneToOne(mappedBy = "utente", cascade = CascadeType.ALL)
    private Borsellino borsellino;

    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL)
    private List<Prenotazione> prenotazioni;

    
}
