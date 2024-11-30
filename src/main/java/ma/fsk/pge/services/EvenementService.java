package ma.fsk.pge.services;

import jakarta.persistence.EntityNotFoundException;
import ma.fsk.pge.entities.Evenement;
import ma.fsk.pge.repository.EvenementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EvenementService {

    @Autowired
    private EvenementRepository evenementRepository;

    public Evenement creerEvenement(Evenement evenement) {
        return evenementRepository.save(evenement);
    }

    public List<Evenement> listerEvenements() {
        return evenementRepository.findAll();
    }

    public Evenement obtenirEvenementParId(Long id) {
        return evenementRepository.findById(id).orElse(null);
    }

    public Evenement modifierEvenement(Long id, Evenement evenement) {
        Evenement existant = obtenirEvenementParId(id);
        existant.setTitre(evenement.getTitre());
        existant.setDescription(evenement.getDescription());
        existant.setDate(evenement.getDate());
        existant.setLieu(evenement.getLieu());
        existant.setCapacite(evenement.getCapacite());
        return evenementRepository.save(existant);
    }


    public void supprimerEvenement(Long id) {
        evenementRepository.deleteById(id);
    }
}
