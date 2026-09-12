package noemicoppotelli.ariadne.service;

import noemicoppotelli.ariadne.entities.PasswordResetToken;
import noemicoppotelli.ariadne.entities.Utente;
import noemicoppotelli.ariadne.exceptions.BadRequestException;
import noemicoppotelli.ariadne.repositories.PasswordResetTokenRepository;
import noemicoppotelli.ariadne.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class PasswordResetService {

    @Value("${app.frontend-url}")
    private String frontendUrl;

    private final UtenteRepository utenteRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    public PasswordResetService(UtenteRepository utenteRepository, PasswordResetTokenRepository tokenRepository,
                                 PasswordEncoder passwordEncoder, MailService mailService) {
        this.utenteRepository = utenteRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    // Non rivela mai se l'email esiste o no: stessa risposta in entrambi i casi
    // (evita di far scoprire a chi chiama quali email sono registrate).
    public void richiediReset(String email) {
        utenteRepository.findByEmail(email).ifPresent(utente -> {
            SecureRandom secureRandom = new SecureRandom();
            byte[] randomBytes = new byte[32];
            secureRandom.nextBytes(randomBytes);
            String tokenGrezzo = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

            PasswordResetToken token = new PasswordResetToken(
                    null, null, utente, LocalDateTime.now().plusHours(1), false, LocalDateTime.now()
            );
            token.setTokenHash(tokenGrezzo);
            tokenRepository.save(token);

            String link = frontendUrl + "/reset-password?token=" + tokenGrezzo;
            mailService.inviaResetPassword(utente.getEmail(), link);
        });
    }

    public void resettaPassword(String tokenGrezzo, String nuovaPassword) {
        String hash = noemicoppotelli.ariadne.entities.RefreshToken.hash(tokenGrezzo);
        PasswordResetToken token = tokenRepository.findByTokenHash(hash)
                .orElseThrow(() -> new BadRequestException("Link di reset non valido."));

        if (token.isUsato() || token.isScaduto(LocalDateTime.now())) {
            throw new BadRequestException("Link di reset non valido o scaduto.");
        }

        Utente utente = token.getUtente();
        utente.setPassword(nuovaPassword, passwordEncoder);
        utenteRepository.save(utente);

        token.segnaUsato();
        tokenRepository.save(token);
    }
}
