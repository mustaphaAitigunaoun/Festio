package ma.fsk.pge.web;

import ma.fsk.pge.entities.Evenement;
import ma.fsk.pge.entities.Participant;
import ma.fsk.pge.services.EvenementService;
import ma.fsk.pge.services.ParticipantService;
import ma.fsk.pge.services.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    @PreAuthorize("hasRole('ORGANISATEUR') or hasRole('PARTICIPANT')")
    public String getParticipantPage(@PathVariable Long eventId,Model model) {
        Evenement evenement = evenementService.obtenirEvenementParId(eventId);
        model.addAttribute("evenement", evenement);
        return "register_participant";
    }

    @PostMapping("/{eventId}/register/{participantNom}")
    @PreAuthorize("hasRole('ORGANISATEUR') or hasRole('PARTICIPANT')")
    public String registerParticipant(@PathVariable Long eventId, @ModelAttribute Participant participant,
                                      @PathVariable String participantNom,Model model) {
        Evenement evenement = evenementService.obtenirEvenementParId(eventId);
        Participant participant1 = participantService.findParticipantByNom(participantNom);

        if (evenement == null) {
            model.addAttribute("message", "Événement non trouvé.");
            return "register_participant";
        }

        if(participant1 != null ){
            model.addAttribute("message", "Participant Exist déja.");
            return "register_participant";
        }

        if (evenement.getParticipants().size() < evenement.getCapacite()) {
            participant.setEvenement(evenement);
            participant.setWaitlisted(false);
            participant.setVerified(false);
            participant.setPayed(false);
            sendMailService.sendVerificationEmail(participant.getEmail(),participantNom,"participant");
            participantService.saveParticipant(participant);
            model.addAttribute("message", "Aprés Paiements please verifier votre Email!");
        } else {
            participant.setEvenement(evenement);
            participant.setWaitlisted(true);
            participant.setVerified(false);
            participant.setPayed(false);
            sendMailService.sendVerificationEmail(participant.getEmail(),participantNom,"waitlisted");
            participantService.saveParticipant(participant);
            model.addAttribute("message", "Capacité atteinte. Vous êtes sur la liste d'attente ! Verifier votre inscription !");
        }


        model.addAttribute("evenement", evenement);
        return "index";
    }

    // List of registered participants
    @GetMapping("/{eventId}/participants")
    @PreAuthorize("hasRole('ORGANISATEUR') or hasRole('PARTICIPANT')")
    public String getParticipants(@PathVariable Long eventId,Model model) {
        Evenement evenement = evenementService.obtenirEvenementParId(eventId);

        if (evenement == null) {
            return "evenements";
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneDayBeforeDeadline = evenement.getDate().minusDays(1);

        if (now.isAfter(oneDayBeforeDeadline) && now.isBefore(evenement.getDate())) {
            // Send reminder emails to all participants
            evenement.getParticipants().forEach(participant -> {
                if (!participant.isVerified()) {
                    // just participant for now after that we gonna handle waitlist
                    sendMailService.sendVerificationEmail(participant.getEmail(),participant.getNom(),"participant");

                }
            });
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

        if(!registered.isEmpty() || !waitlisted.isEmpty()) {
            registered.forEach(participant -> {
                if (!participant.isPayed() && participant.getEvenement().getDate().plusHours(24).isBefore(LocalDateTime.now())) {
                    participantService.deleteParticipantById(participant.getId());
                }
            });

            waitlisted.forEach(participant -> {
                if (!participant.isPayed() && participant.getEvenement().getDate().plusHours(24).isBefore(LocalDateTime.now())) {
                    participantService.deleteParticipantById(participant.getId());
                }
            });
        }

        model.addAttribute("registered",registered);
        model.addAttribute("waitlisted",waitlisted);

        return "participants";
    }




    // Move participant from waitlist to registered
    @PostMapping("/{eventId}/waitlist-to-registered/{participantId}")
    @PreAuthorize("hasRole('ORGANISATEUR')")
    public String moveToRegistered(@PathVariable Long eventId, @PathVariable Long participantId,Model model) {
        Evenement evenement = evenementService.obtenirEvenementParId(eventId);
        Participant participant = participantService.findParticipantById(participantId);

        if (evenement == null || participant == null) {
            model.addAttribute("error","Événement ou participant non trouvé");
        }

        if (!participant.isWaitlisted()) {
            model.addAttribute("error","Le participant n'est pas sur la liste d'attente");
        }
        evenement.setCapacite(evenement.getCapacite() + 1);
        // Check if there is space in the event
        if (evenement.getParticipants().size() <= evenement.getCapacite()) {
            participant.setWaitlisted(false);
            participantService.saveParticipant(participant);
            model.addAttribute("success","Participant déplacé à la liste des inscrits");
            return "evenements";
        } else {
            model.addAttribute("error","La capacité de l'événement est atteinte");
            return "evenements";
        }
    }


    @DeleteMapping("/participant/{participantId}")
    public String deleteParticipant(@PathVariable Long participantId) {
        participantService.deleteParticipantById(participantId);
        return "participants";
    }


}
