package ma.fsk.pge.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import ma.fsk.pge.entities.Evenement;
import ma.fsk.pge.entities.Participant;
import ma.fsk.pge.services.EvenementService;
import ma.fsk.pge.services.ParticipantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = ParticipantService.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ParticipantControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EvenementService evenementService;

    @MockBean
    private ParticipantService participantService;

    @Autowired
    private ObjectMapper objectMapper;


    private Participant participant;
    private Evenement evenement;

    // pour pas repeter le code

    @BeforeEach
    public void init() {
        participant = Participant.builder()
                .nom("hassan")
                .email("hassan@gmail.com")
                .evenement(null)
                .isPayed(true)
                .isVerified(true)
                .isWaitlisted(false)
                .paiement(null)
                .build();

        evenement = Evenement.builder()
                .titre("Evenement1")        // Titre de l'événement
                .lieu("Casablanca")        // Lieu de l'événement
                .description("Evenement 1") // Description de l'événement
                .capacite(10)              // Capacité maximale de l'événement
                .date(LocalDateTime.now()) // Date actuelle pour l'événement
                .participants(null)        // Aucun participant pour l'instant
                .build();
    }



    @Test
    public void EvenementController_GetByID_ReturnsEvenement() throws Exception {

        when(evenementService.obtenirEvenementParId(1L)).thenReturn(evenement);

        // Test GET /api/evenements/1
        mockMvc.perform(get("/evenements/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.titre").value("Titre"))
                .andExpect(jsonPath("$.lieu").value("Casablanca"));
    }
}
