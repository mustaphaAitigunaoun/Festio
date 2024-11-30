package ma.fsk.pge.services;

import ma.fsk.pge.entities.Evenement;
import ma.fsk.pge.entities.Participant;
import ma.fsk.pge.repository.EvenementRepository;
import ma.fsk.pge.repository.ParticipantRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParticipantServiceTests {

    // on a besion de travailler avec 2 repository
    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private EvenementRepository evenementRepository;

    // one service
    @InjectMocks
    private ParticipantService participantService;


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

    // avant d'appeler une methode de service (creer,update,...)
    // en a besion de tester la methode de repo mais sans faire un test d'integration
    // solution : mocking

    @Test
    public void ParticipantService_SaveAll_ReturnsParticipant() {

        when(participantRepository.save(Mockito.any(Participant.class))).thenReturn(participant);
        when(evenementRepository.save(Mockito.any(Evenement.class))).thenReturn(evenement);

        Participant savedParticipant = participantService.saveParticipant(participant);

        Assertions.assertThat(savedParticipant).isNotNull();
    }

    @Test
    public void ParticipantService_GetById_ReturnsParticipant() {
        when(participantRepository.findById(1L)).thenReturn(Optional.ofNullable(participant));
        Participant savedParticipant = participantService.findParticipantById(1L);

        Assertions.assertThat(savedParticipant).isNotNull();
    }
}
