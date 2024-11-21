package ma.fsk.pge.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder @ToString
public class Paiement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double montant;
    private String methode;
    private String statut;

    @OneToOne
    @JoinColumn(name = "participant_id")
    private Participant participant;
}
