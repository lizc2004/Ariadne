package noemicoppotelli.ariadne.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void inviaResetPassword(String destinatario, String linkReset) {
        SimpleMailMessage messaggio = new SimpleMailMessage();
        messaggio.setTo(destinatario);
        messaggio.setSubject("Ariadne — reimposta la tua password");
        messaggio.setText(
                "Hai richiesto di reimpostare la password del tuo account Ariadne.\n\n" +
                "Clicca sul link seguente per sceglierne una nuova (valido per 1 ora):\n" +
                linkReset + "\n\n" +
                "Se non hai richiesto tu questo reset, ignora pure questa email."
        );
        mailSender.send(messaggio);
    }
}
