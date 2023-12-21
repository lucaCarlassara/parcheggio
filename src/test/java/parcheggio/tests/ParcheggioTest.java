package parcheggio.tests;

import jakarta.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import parcheggio.parc.CreateParkingRequest;
import parcheggio.parc.Parcheggio;
import parcheggio.parc.ParcheggioService;
import parcheggio.utenze.Gestore;
import parcheggio.utenze.PersonaRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ParcheggioTest {

    @Autowired
    private ParcheggioService parcheggioService;

    @MockBean
    private PersonaRepository personaRepository;

    @Test
    public void testCreateParking() {
        // Arrange
        String gestoreEmail = "gestore@example.com";
        CreateParkingRequest createParkingRequest = new CreateParkingRequest();
        createParkingRequest.setCitta("City");
        createParkingRequest.setCap("12345");
        createParkingRequest.setIndirizzo("Address");
        createParkingRequest.setNumeroPosti(50);
        createParkingRequest.setFoto("parking.jpg");
        createParkingRequest.setTariffaCoperto(10);
        createParkingRequest.setTariffaScoperto(8);
        createParkingRequest.setTariffaSUV(15);
        createParkingRequest.setTariffaUtilitaria(12);

        Gestore gestore = new Gestore();
        gestore.setEmail(gestoreEmail);

        Optional<Gestore> optional = Optional.of(gestore);
        when(personaRepository.findByEmail(gestoreEmail)).thenReturn(Optional.of(gestore));

        // Act
        parcheggioService.createParking(gestoreEmail, createParkingRequest);

        // Assert
        verify(personaRepository, times(1)).findByEmail(gestoreEmail);
        verify(personaRepository, times(1)).save(gestore);
        assertNotNull(gestore.getParcheggi());
        assertEquals(1, gestore.getParcheggi().size());

        Parcheggio createdParcheggio = gestore.getParcheggi().get(0);
        assertEquals("City", createdParcheggio.getCitta());
        assertEquals("12345", createdParcheggio.getCap());
        assertEquals("Address", createdParcheggio.getIndirizzo());
        assertEquals(50, createdParcheggio.getNumeroPosti());
        assertEquals("parking.jpg", createdParcheggio.getFoto());
        assertEquals(10, createdParcheggio.getTariffaCoperto());
        assertEquals(8, createdParcheggio.getTariffaScoperto());
        assertEquals(15, createdParcheggio.getTariffaSUV());
        assertEquals(12, createdParcheggio.getTariffaUtilitaria());
        assertEquals(gestore, createdParcheggio.getGestore());
    }

    @Test
    public void testDeleteParking() {
        // Arrange
        String gestoreEmail = "gestore@example.com";
        Long parkingId = 1L;

        Gestore gestore = new Gestore();
        gestore.setEmail(gestoreEmail);

        Parcheggio parcheggio = Parcheggio.builder()
                .id(parkingId)
                .citta("City")
                .cap("12345")
                .indirizzo("Address")
                .numeroPosti(50)
                .foto("parking.jpg")
                .tariffaCoperto(10)
                .tariffaScoperto(8)
                .tariffaSUV(15)
                .tariffaUtilitaria(12)
                .gestore(gestore)
                .build();

        gestore.getParcheggi().add(parcheggio);

        when(personaRepository.findByEmail(gestoreEmail)).thenReturn(Optional.of(gestore));

        // Act
        parcheggioService.deleteParking(gestoreEmail, parkingId);

        // Assert
        verify(personaRepository, times(1)).findByEmail(gestoreEmail);
        verify(personaRepository, times(1)).save(gestore);
        assertNotNull(gestore.getParcheggi());
        assertEquals(0, gestore.getParcheggi().size());
    }
}
