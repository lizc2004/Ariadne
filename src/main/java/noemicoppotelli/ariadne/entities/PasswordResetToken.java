package noemicoppotelli.ariadne.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @JsonIgnore
    private String tokenHash;

    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;

    @Column(nullable = false)
    private LocalDateTime dataScadenza;

    @Column(nullable = false)
    private boolean usato;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public void segnaUsato() {
        this.usato = true;
    }

    public boolean isScaduto(LocalDateTime adesso) {
        return adesso.isAfter(dataScadenza);
    }

    public void setTokenHash(String tokenHash) {
        this.tokenHash = RefreshToken.hash(tokenHash);
    }
}
