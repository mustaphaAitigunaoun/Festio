package ma.fsk.pge.services;

import ma.fsk.pge.entities.Evenement;
import ma.fsk.pge.repository.EvenementRepository;
import org.assertj.core.api.Assertions;
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
// Utilise l'extension Mockito pour configurer les tests avec Mockito.
// Elle permet d'utiliser les annotations comme @Mock et @InjectMocks sans configuration supplémentaire.
public class EvenementServiceTests {

    @Mock
    private EvenementRepository evenementRepository;
    // Crée une instance fictive (mockée) de `EvenementRepository`.
    // Cette instance est utilisée pour simuler les interactions avec la base de données
    // sans réellement exécuter de requêtes.

    @InjectMocks
    private EvenementService evenementService;
    // Injecte automatiquement le mock `evenementRepository` dans `evenementService`.
    // Cela permet de tester `EvenementService` indépendamment de la base de données réelle.

    @Test
    public void EvenementService_SaveAll_ReturnsSavedEvenement() {
        // Indique qu'il s'agit d'un cas de test. JUnit exécute cette méthode comme un test unitaire.

        // Création d'un objet `Evenement` avec les valeurs nécessaires.
        Evenement evenement = Evenement.builder()
                .titre("Evenement1")        // Titre de l'événement
                .lieu("Casablanca")        // Lieu de l'événement
                .description("Evenement 1") // Description de l'événement
                .capacite(10)              // Capacité maximale de l'événement
                .date(LocalDateTime.now()) // Date actuelle pour l'événement
                .participants(null)        // Aucun participant pour l'instant
                .build();

        // Configure le comportement du mock `evenementRepository`.
        // Lorsque la méthode `save` est appelée avec n'importe quel objet `Evenement`,
        // le mock retournera l'objet `evenement` défini plus tôt.
        when(evenementRepository.save(Mockito.any(Evenement.class)))
                .thenReturn(evenement);

        // Appelle la méthode à tester dans `EvenementService`.
        // Cette méthode utilise le mock `evenementRepository` pour simuler l'enregistrement.
        Evenement savedEvenement1 = evenementService.creerEvenement(evenement);

        // Vérifie que l'objet retourné par la méthode `creerEvenement` n'est pas `null`.
        // Cela signifie que l'événement a été correctement "enregistré".
        Assertions.assertThat(savedEvenement1).isNotNull();
    }


    @Test
    public void EvenementService_GetByID_ReturnsEvenement() {
        Evenement evenement = Evenement.builder()
                .titre("Evenement1")
                .lieu("Casablanca")
                .description("Evenement 1")
                .capacite(10)
                .date(LocalDateTime.now())
                .participants(null)
                .build();
        when(evenementRepository.findById(1L)).thenReturn(Optional.ofNullable(evenement));
        Evenement savedEvenement = evenementService.obtenirEvenementParId(1L);

        Assertions.assertThat(savedEvenement).isNotNull();

    }

    @Test
    public void EvenementService_UpdateEvenement_ReturnsEvenement() {
        Evenement evenement = Evenement.builder()
                .titre("Evenement1")
                .lieu("Casablanca")
                .description("Evenement 1")
                .capacite(10)
                .date(LocalDateTime.now())
                .participants(null)
                .build();

        when(evenementRepository.findById(1L)).thenReturn(Optional.ofNullable(evenement));
        Evenement savedEvenement = evenementService.obtenirEvenementParId(1L);
        savedEvenement.setTitre("Evenement 1 updated");
        savedEvenement.setDescription("Evenement 1 desc updated");
        // if you have update in service you need to change this
        when(evenementRepository.save(Mockito.any(Evenement.class))).thenReturn(savedEvenement);
        // if you have update in service you need to change this
        Evenement savedUpdatedEvenement = evenementService.creerEvenement(savedEvenement);

        Assertions.assertThat(savedUpdatedEvenement).isNotNull();

    }
}

