package noemicoppotelli.ariadne.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import noemicoppotelli.ariadne.entities.Utente;
import noemicoppotelli.ariadne.entities.RefreshToken;
import noemicoppotelli.ariadne.repositories.RefreshTokenRepository;
import noemicoppotelli.ariadne.exceptions.UnauthorizedException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.time.LocalDateTime;

@Service
public class RefreshTokenService {
    @Value("${refresh.token.expiration}")
    private Long expiration;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }


    public String creaRefreshToken(Utente utente) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String tokenGrezzo = Base64.getEncoder().encodeToString(randomBytes);

        LocalDateTime dataScadenza = LocalDateTime.now().plus(Duration.ofMillis(expiration));
        RefreshToken refreshToken = new RefreshToken(null, null, utente, dataScadenza, false, LocalDateTime.now());
        refreshToken.setTokenHash(tokenGrezzo);
        refreshTokenRepository.save(refreshToken);
        return tokenGrezzo;
    }

    private RefreshToken getRefreshToken(String tokenGrezzo) {
        String hash = RefreshToken.hash(tokenGrezzo);
        return refreshTokenRepository.findByTokenHash(hash)
                .orElseThrow(() -> new UnauthorizedException("Refresh token non trovato"));
    }

    public Utente validaRefreshToken(String tokenGrezzo) {
        RefreshToken refreshToken = getRefreshToken(tokenGrezzo);
        if (refreshToken.isRevocato() || refreshToken.isScaduto(LocalDateTime.now())) {
            throw new UnauthorizedException("Refresh token non valido o scaduto.");
        }
        return refreshToken.getUtente();
    }

    public void revocaRefreshToken(String tokenGrezzo) {
        RefreshToken refreshToken = getRefreshToken(tokenGrezzo);
        refreshToken.revoca();
        refreshTokenRepository.save(refreshToken);
    }

}



