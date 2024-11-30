package ma.fsk.pge.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String nom;
    private String email;
    private boolean isWaitlisted;
    private boolean isVerified;
    private boolean isPayed;

    @ManyToOne
    @JoinColumn(name = "evenement_id")
    private Evenement evenement;

    @OneToOne(mappedBy = "participant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Paiement paiement;
}
