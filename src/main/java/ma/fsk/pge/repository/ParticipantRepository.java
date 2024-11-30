package ma.fsk.pge.repository;

import ma.fsk.pge.entities.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant,Long> {
    public Participant findByNom(String nom);
}
