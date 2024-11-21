package ma.fsk.pge.repository;

import ma.fsk.pge.entities.Evenement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvenementRepository extends JpaRepository<Evenement,Long> {

}
