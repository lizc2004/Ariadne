package noemicoppotelli.ariadne.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "refresh_tokens")

public class RefreshToken {
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
    private boolean revocato;

    public void revoca() {
        this.revocato = true;
    }
    public boolean isScaduto(LocalDateTime adesso) {
        return adesso.isAfter(dataScadenza);
    }

    @Column(nullable = false)
    private LocalDateTime createdAt;


    public void setTokenHash(String tokenHash, PasswordEncoder passwordEncoder) {
        this.tokenHash = passwordEncoder.encode(tokenHash);
    }
}



