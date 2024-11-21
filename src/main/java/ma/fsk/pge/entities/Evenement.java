package ma.fsk.pge.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder @ToString
public class Evenement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;
    private LocalDateTime date;
    private String lieu;
    private int capacite;

    @OneToMany(mappedBy = "evenement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants = new ArrayList<>();
}
