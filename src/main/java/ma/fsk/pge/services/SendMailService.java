package ma.fsk.pge.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SendMailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendVerificationEmail(String to, Long participantId, String type) {
        try {
            // Construct the verification URL dynamically
            String verificationUrl = "http://localhost:8085/verify-email?participantId=" + participantId;

            // Prepare the email body with HTML
            StringBuilder body = new StringBuilder();
            body.append("<h1>Festio</h1>");
            body.append("<p>Click the link below to verify your email:</p>");
            body.append("<a href=\"").append(verificationUrl).append("\">Verify Email</a>");
            body.append("<hr>");
            if ("participant".equalsIgnoreCase(type)) {
                body.append("<p>You are in the <b>Participant List</b>.</p>");
            } else {
                body.append("<p>You are in the <b>Wait List Participant</b>.</p>");
            }
            body.append("<p>If you did not sign up for this event, please ignore this email.</p>");

            // Create a MimeMessage for sending HTML content
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("Participant Email Verification");
            helper.setText(body.toString(), true); // Enable HTML content

            // Send the email
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send verification email", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
