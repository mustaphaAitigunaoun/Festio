package ma.fsk.pge.repository;

import ma.fsk.pge.entities.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaimentRepository extends JpaRepository<Paiement,Long> {
}
