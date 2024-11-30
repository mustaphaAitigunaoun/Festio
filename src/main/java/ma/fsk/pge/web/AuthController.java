package ma.fsk.pge.web;

import ma.fsk.pge.entities.Participant;
import ma.fsk.pge.services.ParticipantService;
import ma.fsk.pge.services.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {


    @Autowired
    private SendMailService sendMailService;

    @Autowired
    private ParticipantService participantService;

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/a-propos")
    public String aPropos() {
        return "a_propos";
    }

    @GetMapping("/contact")
    public String Contact() {
        return "contact";
    }

    @PostMapping("/contact")
    public String CreerContact(@RequestParam String name,@RequestParam String email
            ,@RequestParam String message,Model model) {
        if(name == null || email == null || message == null) {
            model.addAttribute("message","some fields are empty!");
            return "contact";
        }
        sendMailService.contact(email,message,name);
        model.addAttribute("message","your message has been sent!");
        return "contact";
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/profile/{name}")
    public String profile(@PathVariable String name, Model model) {
        Participant participant = participantService.findParticipantByNom(name);
        if(participant == null) {
            return "evenements";
        }
        model.addAttribute("participant",participant);
        return "profile";
    }
}
