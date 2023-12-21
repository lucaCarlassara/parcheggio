package parcheggio.tests;

import jakarta.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import parcheggio.borsellino.Borsellino;
import parcheggio.borsellino.BorsellinoService;
import parcheggio.utenze.Gestore;
import parcheggio.utenze.PersonaRepository;
import parcheggio.utenze.Utente;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class BorsellinoTest {

    @Autowired
    private BorsellinoService borsellinoService;

    @MockBean
    private PersonaRepository personaRepository;

    @Test
    public void testCaricaBorsellino() {
        // Arrange
        String userEmail = "user@example.com";
        Double initialBalance = 50.0;
        Double amountToLoad = 30.0;

        Utente utente = Utente.builder()
                .email(userEmail)
                .borsellino(Borsellino.builder().saldo(initialBalance).build())
                .build();

        Optional<Utente> optional = Optional.of(utente);
        when(personaRepository.findByEmail(userEmail)).thenReturn(Optional.of(utente));

        // Act
        borsellinoService.caricaBorsellino(userEmail, amountToLoad);

        // Assert
        verify(personaRepository, times(1)).findByEmail(userEmail);
        verify(personaRepository, times(1)).save(utente);

        Borsellino updatedBorsellino = utente.getBorsellino();
        assertNotNull(updatedBorsellino);
        assertEquals(initialBalance + amountToLoad, updatedBorsellino.getSaldo(), 0.01); // Use delta for double comparison
    }

    @Test
    public void testGetSaldo() {
        // Arrange
        String userEmail = "user@example.com";
        Double expectedSaldo = 50.0;

        Utente utente = Utente.builder()
                .email(userEmail)
                .borsellino(Borsellino.builder().saldo(expectedSaldo).build())
                .build();

        when(personaRepository.findByEmail(userEmail)).thenReturn(Optional.of(utente));

        // Act
        Double actualSaldo = borsellinoService.getSaldo(userEmail);

        // Assert
        verify(personaRepository, times(1)).findByEmail(userEmail);

        assertEquals(expectedSaldo, actualSaldo, 0.01); // Use delta for double comparison
    }
}
