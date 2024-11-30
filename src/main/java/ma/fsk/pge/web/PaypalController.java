package ma.fsk.pge.web;


import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.fsk.pge.entities.Participant;
import ma.fsk.pge.repository.PaimentRepository;
import ma.fsk.pge.repository.ParticipantRepository;
import ma.fsk.pge.services.PaypalService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PaypalController {
    private final PaypalService paypalService;
    private final ParticipantRepository participantRepository; // Injected using @RequiredArgsConstructor
    private final PaimentRepository paimentRepository; // Injected using @RequiredArgsConstructor

    @GetMapping("/payment")
    public String home() {
        return "index";
    }

    @PostMapping("/payment/create")
    public RedirectView createPayment() {
        try {
            String cancelURL = "http://localhost:8085/payment/cancel";
            String successURL = "http://localhost:8085/payment/success";

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Get the username of the authenticated user
            String username = authentication.getName();

            // Retrieve the participant by their username
            Participant participant = participantRepository.findByNom(username);


            Payment payment = paypalService.payment(
                    10.0,
                    "USD",
                    "paypal",
                    "sale",
                    "Payment Description",
                    cancelURL,
                    successURL,
                    participant
            );

            for(Links links : payment.getLinks()) {
                if(links.getRel().equals("approval_url")){
                    return new RedirectView(links.getHref());
                }
            }

        }catch (PayPalRESTException e) {
            log.error("error payments :: ",e);
        }
        return new RedirectView("/payment/error");
    }

    @GetMapping("/payment/success")
    public String successPayment(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId // Corrected the parameter name to match PayPal's response
    ) {
        try {
            // Execute the payment
            Payment payment = paypalService.paymentExecute(paymentId, payerId);

            // Retrieve the authentication object
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Get the username of the authenticated user
            String username = authentication.getName();

            // Retrieve the participant by their username
            Participant participant = participantRepository.findByNom(username);
            if (participant != null) {
                // Set the payment participant and mark payment as successful
                participant.setPayed(true);
                participantRepository.save(participant);
            }

            if ("approved".equals(payment.getState())) {
                return "successPayment"; // Return the success view
            }
        } catch (PayPalRESTException e) {
            log.error("Error during payment success handling: ", e);
        }
        return "errorPayment"; // Return error view if something goes wrong
    }

    @GetMapping("/payment/cancel")
    public String cancelPayment() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the username of the authenticated user
        String username = authentication.getName();

        // Retrieve and delete the participant and their payment
        Participant participant = participantRepository.findByNom(username);
        if (participant != null) {
            paimentRepository.deleteById(participant.getPaiement().getId());
            participantRepository.deleteById(participant.getId());
        }

        return "cancelPayment"; // Return the cancellation view
    }

    @GetMapping("/payment/error")
    public String errorPayment() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the username of the authenticated user
        String username = authentication.getName();

        // Retrieve and delete the participant and their payment
        Participant participant = participantRepository.findByNom(username);
        if (participant != null) {
            paimentRepository.deleteById(participant.getPaiement().getId());
            participantRepository.deleteById(participant.getId());
        }

        return "errorPayment"; // Return the error view
    }
}
