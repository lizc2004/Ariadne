package noemicoppotelli.ariadne.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "cards", indexes = {
        @Index(name = "idx_card_deck_id", columnList = "deck_id"),
        @Index(name = "idx_card_next_review", columnList = "prossima_revisione")
})

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fronte;

    @Column(nullable = false)
    private String retro;

    @ManyToOne
    @JoinColumn(name = "deck_id")
    private Deck deck;

    @Column(nullable = false)
    private float ease;

    @Column(nullable = false)
    private int intervallo;

    @Column(nullable = false)
    private int ripetizioni;

    @Column(nullable = false)
    private LocalDate prossimaRevisione;

    @Column
    private LocalDate ultimaRevisione;
}
