package noemicoppotelli.ariadne.entities;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import noemicoppotelli.ariadne.enums.Priorita;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titolo;

    @Column(nullable = false)
    private String materia;

    @Column(nullable = false)
    private LocalDate scadenza ;

    @Column(nullable = false)
    private boolean completato;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priorita priorita;

    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;
}
