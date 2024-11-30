package ma.fsk.pge.repository;

import ma.fsk.pge.entities.Participant;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ParticipantRepositoryTests {

    @Autowired
    private ParticipantRepository participantRepository;

    @Test
    public void ParticipantRepository_SaveAll_ReturnsParticipant() {
        Participant participant = Participant.builder()
                .nom("hassan")
                .email("hassan@gmail.com")
                .evenement(null)
                .isPayed(true)
                .isVerified(true)
                .isWaitlisted(false)
                .paiement(null)
                .build();
        Participant savedParticipant = participantRepository.save(participant);

        Assertions.assertThat(savedParticipant).isNotNull();
    }


    @Test
    public void ParticipantRepository_GetByID_ReturnsParticipant() {
        Participant participant = Participant.builder()
                .nom("hassan")
                .email("hassan@gmail.com")
                .evenement(null)
                .isPayed(true)
                .isVerified(true)
                .isWaitlisted(false)
                .paiement(null)
                .build();
        participantRepository.save(participant);

        Participant savedParticipant = participantRepository.findById(participant.getId()).get();

        Assertions.assertThat(savedParticipant).isNotNull();
    }
}
