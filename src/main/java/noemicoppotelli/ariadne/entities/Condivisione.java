package noemicoppotelli.ariadne.entities;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import noemicoppotelli.ariadne.enums.StatoCondivisione;


@Entity
@Table(name = "condivisioni")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Condivisione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Utente owner;

    @ManyToOne
    @JoinColumn(name = "viewer_id")
    private Utente viewer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoCondivisione stato;

    @Column(nullable = false)
    private LocalDateTime createdAt;

}
