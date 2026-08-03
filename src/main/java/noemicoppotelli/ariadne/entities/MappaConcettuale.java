package noemicoppotelli.ariadne.entities;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mappe_concettuali")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


public class MappaConcettuale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenutoMermaid;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String testoOriginale;

    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;

    @Column(nullable = false)
    private LocalDateTime createdAt;

}

