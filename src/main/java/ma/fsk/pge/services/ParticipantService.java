package ma.fsk.pge.services;

import ma.fsk.pge.entities.Evenement;
import ma.fsk.pge.entities.Participant;
import ma.fsk.pge.repository.EvenementRepository;
import ma.fsk.pge.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ParticipantService {
    @Autowired
    private ParticipantRepository participantRepository;

    public Participant saveParticipant(Participant participant) {
        return participantRepository.save(participant);
    }

    public Participant findParticipantById(Long id) {
        Participant participant = participantRepository.findById(id).orElse(null);
        if( participant == null ){
            return null;
        }
        return participant;
    }
}
