package ma.fsk.pge.services;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import ma.fsk.pge.entities.Paiement;
import ma.fsk.pge.entities.Participant;
import ma.fsk.pge.repository.PaimentRepository;
import ma.fsk.pge.repository.ParticipantRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PaypalService {

    private final APIContext apiContext;
    private final PaimentRepository paimentRepository; // Ensure injected via constructor
    private final ParticipantRepository participantRepository;

    public Payment payment(
            Double total,
            String currency,
            String method,
            String intent,
            String description,
            String cancelURL,
            String successURL,
            Participant participant
    ) throws PayPalRESTException {

        // Create the payment object
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format(Locale.forLanguageTag(currency), "%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method);

        Payment payment = new Payment();
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(transactionList);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelURL);
        redirectUrls.setReturnUrl(successURL);

        payment.setRedirectUrls(redirectUrls);

        // Save the payment details to the database
        Paiement paiement = Paiement.builder()
                .total(total)
                .currency(currency)
                .method(method)
                .intent(intent)
                .description(description)
                .participant(participant)
                .build();

        paimentRepository.save(paiement); // Persist the payment details

        return payment.create(apiContext);
    }

    public Payment paymentExecute(
            String paymentId,
            String payerId
    ) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        return payment.execute(apiContext, paymentExecution);
    }
}

