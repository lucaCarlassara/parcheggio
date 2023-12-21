package parcheggio.utenze;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;
import parcheggio.parc.Parcheggio;

import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder(toBuilder = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "gestore_id")
public class Gestore extends Persona {

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @NotNull
    private String ragioneSociale;

    @NonNull
    @Pattern(regexp = "\\d{11}", message = "Formato Partita IVA non valido")
    private String partitaIVA;

    @NotNull
    @Pattern(regexp = "[A-Z0-9]{16}", message = "Formato Codice Fiscale non valido")
    private String codiceFiscale;

    @NotNull
    private String nickname;

    @OneToMany(mappedBy = "gestore", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Parcheggio> parcheggi = new ArrayList<>();

}
