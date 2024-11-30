package ma.fsk.pge.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder @ToString
public class Paiement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double total;
    private String currency;
    private String method;
    private String intent;
    private String description;

    @OneToOne
    @JoinColumn(name = "participant_id")
    private Participant participant;
}
