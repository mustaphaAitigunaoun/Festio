package ma.fsk.pge.web;

import ma.fsk.pge.entities.Evenement;
import ma.fsk.pge.entities.Participant;
import ma.fsk.pge.services.EvenementService;
import ma.fsk.pge.services.ParticipantService;
import ma.fsk.pge.services.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/evenements")
public class ParticipantController {
    @Autowired
    private EvenementService evenementService;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private SendMailService sendMailService;



    @GetMapping("/{eventId}/register")
    public String getParticipantPage(@PathVariable Long eventId,Model model) {
        Evenement evenement = evenementService.obtenirEvenementParId(eventId);
        model.addAttribute("evenement", evenement);
        return "register_participant";
    }

    @PostMapping("/{eventId}/register")
    public String registerParticipant(@PathVariable Long eventId, @ModelAttribute Participant participant, Model model) {
        Evenement evenement = evenementService.obtenirEvenementParId(eventId);
        if (evenement == null) {
            model.addAttribute("message", "Événement non trouvé.");
            return "register_participant";
        }

        if (evenement.getParticipants().size() < evenement.getCapacite()) {
            participant.setEvenement(evenement);
            participant.setWaitlisted(false);
            participant.setVerified(false);
            participantService.saveParticipant(participant);
            sendMailService.sendVerificationEmail(participant.getEmail(),participant.getId(),"participant");
            model.addAttribute("message", "Inscription réussie !");
        } else {
            participant.setEvenement(evenement);
            participant.setWaitlisted(true);
            participant.setVerified(false);
            participantService.saveParticipant(participant);
            sendMailService.sendVerificationEmail(participant.getEmail(),participant.getId(),"waitlisted");
            model.addAttribute("message", "Capacité atteinte. Vous êtes sur la liste d'attente.");
        }

        model.addAttribute("evenement", evenement);
        return "register_participant";
    }

    // List of registered participants
    @GetMapping("/{eventId}/participants")
    public String getParticipants(@PathVariable Long eventId,Model model) {
        Evenement evenement = evenementService.obtenirEvenementParId(eventId);

        if (evenement == null) {
            return "evenements";
        }

        // Separate registered and waitlisted participants
        List<Participant> registered = evenement.getParticipants()
                .stream()
                .filter(p -> !p.isWaitlisted())
                .collect(Collectors.toList());
        List<Participant> waitlisted = evenement.getParticipants()
                .stream()
                .filter(Participant::isWaitlisted)
                .collect(Collectors.toList());

        model.addAttribute("registered",registered);
        model.addAttribute("waitlisted",waitlisted);

        return "participants";
    }




    // Move participant from waitlist to registered
    @PostMapping("/{eventId}/waitlist-to-registered/{participantId}")
    public ResponseEntity<String> moveToRegistered(@PathVariable Long eventId, @PathVariable Long participantId) {
        Evenement evenement = evenementService.obtenirEvenementParId(eventId);
        Participant participant = participantService.findParticipantById(participantId);

        if (evenement == null || participant == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Événement ou participant non trouvé");
        }

        if (!participant.isWaitlisted()) {
            return ResponseEntity.badRequest().body("Le participant n'est pas sur la liste d'attente");
        }

        // Check if there is space in the event
        if (evenement.getParticipants().size() < evenement.getCapacite()) {
            participant.setWaitlisted(false);
            participantService.saveParticipant(participant);
            return ResponseEntity.ok("Participant déplacé à la liste des inscrits");
        } else {
            return ResponseEntity.badRequest().body("La capacité de l'événement est atteinte");
        }
    }
}
