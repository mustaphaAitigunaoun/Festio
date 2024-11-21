package ma.fsk.pge.web;

import ma.fsk.pge.entities.Participant;
import ma.fsk.pge.services.ParticipantService;
import ma.fsk.pge.services.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class SendMailController {

    @Autowired
    private ParticipantService participantService;

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("participantId") Long participantId, Model model) {
        Optional<Participant> optionalParticipant = Optional.ofNullable(participantService.findParticipantById(participantId));

        if (optionalParticipant.isPresent()) {
            Participant participant = optionalParticipant.get();
            participant.setVerified(true); // Change the `verified` status
            participantService.saveParticipant(participant); // Save the user back to the database
            model.addAttribute("message", "Email verified successfully!");
        } else {
            model.addAttribute("message", "Invalid or expired token.");
        }

        return "verification-result"; // Thymeleaf template for result page
    }

}
