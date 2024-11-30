package ma.fsk.pge.repository;

import ma.fsk.pge.entities.Evenement;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class EvenementRepositoryTests {

    @Autowired
    private EvenementRepository evenementRepository;

    @Test
    public void EvenementRepository_SaveAll_ReturnsSavedEvenement(){
        // ARRANGE
        Evenement evenement = Evenement.builder()
                .titre("Evenement1")
                .lieu("Casablanca")
                .description("Evenement 1")
                .capacite(10)
                .date(LocalDateTime.now())
                .participants(null)
                .build();
        // ACT
        Evenement savedEvenement = evenementRepository.save(evenement);

        // Assert

        Assertions.assertThat(savedEvenement).isNotNull();
        Assertions.assertThat(savedEvenement.getId()).isGreaterThan(0);

    }

    @Test
    public void EvenementRepository_GetEvenementById_ReturnsEvenement(){
        // ARRANGE
        Evenement evenement = Evenement.builder()
                .titre("Evenement 2")
                .lieu("Casablanca")
                .description("Evenement 2")
                .capacite(10)
                .date(LocalDateTime.now())
                .participants(null)
                .build();
        evenementRepository.save(evenement);
        // ACT
        Evenement get_evenement = evenementRepository.findById(evenement.getId()).get();
        // Assert
        Assertions.assertThat(get_evenement).isNotNull();
    }

    @Test
    public void EvenementRepository_GetAllEvenements_ReturnsEvenements(){
        // ARRANGE
        Evenement evenement1 = Evenement.builder()
                .titre("Evenement 1")
                .lieu("Casablanca")
                .description("Evenement 1")
                .capacite(10)
                .date(LocalDateTime.now())
                .participants(null)
                .build();
        Evenement evenement2 = Evenement.builder()
                .titre("Evenement 2")
                .lieu("Casablanca")
                .description("Evenement 2")
                .capacite(10)
                .date(LocalDateTime.now())
                .participants(null)
                .build();
        // ACT
        evenementRepository.save(evenement1);
        evenementRepository.save(evenement2);

        List<Evenement> getEvenements = evenementRepository.findAll();
        // Assert
        Assertions.assertThat(getEvenements).isNotNull();
        Assertions.assertThat(getEvenements.stream().count()).isGreaterThan(0);
    }

}
