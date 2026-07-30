package noemicoppotelli.ariadne.entities;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "sessioni")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sessione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String materia;

    @Column (nullable = false)
    private int minuti;

    @Column(nullable = false)
    private LocalDateTime iniziata;

    @Column
    private LocalDateTime completata;

    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;

}
