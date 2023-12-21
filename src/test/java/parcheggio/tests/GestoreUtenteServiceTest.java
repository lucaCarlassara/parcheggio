package parcheggio.tests;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import parcheggio.utente.CambioTargaRequest;
import parcheggio.utente.UtenteService;
import parcheggio.utenze.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class GestoreUtenteServiceTest {

    @Autowired
    private PersonaRepository personaRepository;

    @Mock
    private PersonaRepository personaRepository2;

    @InjectMocks
    private UtenteService utenteService;

    @Test
    public void testCreateGestoreNotNull() {
        // Create a Gestore object with all fields set
        var  gestore = Gestore.builder()
                .nome("GestoreNome")
                .cognome("GestoreCognome")
                .email("gestore@example.com")
                .password("gestorePassword")
                .citta("GestoreCity")
                .cap("12345")
                .indirizzo("GestoreAddress")
                .role(Role.GESTORE)
                .ragioneSociale("GestoreCompany")
                .partitaIVA("12345678901")
                .codiceFiscale("ABC123DEF456GHIJ")
                .nickname("GestoreNickname")
                .build();

        // Save the Gestore
        gestore = personaRepository.save(gestore);

        // Perform assertions to validate the save operation
        assertNotNull(gestore.getId());
        // Add more assertions as needed
    }

    @Test
    public void testCreateGestoreNull() {
        // Create a Gestore object with all fields set
        var  gestore = Gestore.builder()
                .nome("GestoreNome")
                .cognome("GestoreCognome")
                .email("gestore@examplecom")
                .password("gestorePassword")
                .citta("GestoreCity")
                .cap("1234") //sbagliato
                .indirizzo("GestoreAddress")
                .role(Role.GESTORE)
                .ragioneSociale("GestoreCompany")
                .partitaIVA("12345678901Aa") //sbagliato
                .codiceFiscale("ABC123D456GHIJ") //sbagliato
                .nickname("GestoreNickname")
                .build();

        // Save the Gestore
        //gestore = personaRepository.save(gestore);

        // Perform assertions to validate the save operation
        assertNull(gestore.getId());
        // Add more assertions as needed
    }

    @Test
    public void testCreateUtente() {
        // Create an Utente object with all fields set
        var utente = Utente.builder()
                .nome("UtenteNome")
                .cognome("UtenteCognome")
                .email("utente@example.com")
                .password("utentePassword")
                .citta("UtenteCity")
                .cap("54321")
                .indirizzo("UtenteAddress")
                .role(Role.UTENTE)
                .targa("AB123CD")
                .tipologiaAuto(TipologiaAuto.SUV)
                .build();

        // Save the Utente
        utente = personaRepository.save(utente);

        // Perform assertions to validate the save operation
        assertNotNull(utente.getId());
        // Add more assertions as needed
    }

    @Test
    public void testUpdateUtenteTarga() {
        // Arrange
        Utente utente = new Utente(); // Create a dummy Utente object
        utente.setEmail("test@example.com");
        utente.setTarga("11AAA11");

        doReturn(Optional.of(utente)).when(personaRepository2).findByEmail("test@example.com");

        CambioTargaRequest request = new CambioTargaRequest();
        request.setTarga("22BBB22");

        // Act
        utenteService.updateUtente("test@example.com", request);

        // Assert
        verify(personaRepository2, times(1)).save(utente);
        assertEquals("22BBB22", utente.getTarga()); // Add more assertions as needed
    }

    @Test
    public void testUpdateUtenteTargaWithInvalidRequest() {
        // Arrange
        CambioTargaRequest request = new CambioTargaRequest();

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> utenteService.updateUtente("test@example.com", request));
    }

    @Test
    public void testUpdateUtenteTargaNotFound() {
        // Arrange
        CambioTargaRequest request = new CambioTargaRequest();

        when(personaRepository2.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> utenteService.updateUtente("nonexistent@example.com", request));
    }


}
