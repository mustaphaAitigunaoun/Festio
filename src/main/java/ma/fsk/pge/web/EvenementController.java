package ma.fsk.pge.web;

import ma.fsk.pge.entities.Evenement;
import ma.fsk.pge.entities.Participant;
import ma.fsk.pge.services.EvenementService;
import ma.fsk.pge.services.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/evenements")
public class EvenementController {
    @Autowired
    private EvenementService evenementService;

    @Autowired
    private ParticipantService participantService;

    @GetMapping
    @PreAuthorize("hasRole('ORGANISATEUR') or hasRole('PARTICIPANT')")
    public String listerEvenements(Model model) {
        model.addAttribute("evenements", evenementService.listerEvenements());
        return "evenements";
    }



    @GetMapping("/nouveau")
    @PreAuthorize("hasRole('ORGANISATEUR')")
    public String afficherFormulaireCreation(Model model) {
        model.addAttribute("evenement", new Evenement());
        return "creer_evenement";
    }

    @PostMapping
    @PreAuthorize("hasRole('ORGANISATEUR')")
    public String creerEvenement(@ModelAttribute Evenement evenement) {
        evenementService.creerEvenement(evenement);
        return "redirect:/evenements";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ORGANISATEUR') or hasRole('PARTICIPANT')")
    public String detailsEvenement(@PathVariable Long id, Model model) {
        model.addAttribute("evenement", evenementService.obtenirEvenementParId(id));
        return "details_evenement";
    }

    // Modifier un événement (GET form + POST update)
    @GetMapping("/modifier/{id}")
    @PreAuthorize("hasRole('ORGANISATEUR')")
    public String showModifierForm(@PathVariable Long id, Model model) {
        Evenement evenement = evenementService.obtenirEvenementParId(id);
        model.addAttribute("evenement", evenement);
        return "modifier"; // Thymeleaf template name
    }

    @PostMapping("/modifier/{id}")
    @PreAuthorize("hasRole('ORGANISATEUR')")
    public String modifierEvenement(@PathVariable Long id, @ModelAttribute("evenement") Evenement evenement) {
        evenementService.modifierEvenement(id, evenement);
        return "redirect:/evenements"; // Redirect to the list page after updating
    }

    // Supprimer un événement
    @GetMapping("/supprimer/{id}")
    @PreAuthorize("hasRole('ORGANISATEUR')")
    public String supprimerEvenement(@PathVariable Long id) {
        evenementService.supprimerEvenement(id);
        return "redirect:/evenements"; // Redirect to the list page after deletion
    }

}
