package noemicoppotelli.ariadne.payloads;
import lombok.Getter;
import noemicoppotelli.ariadne.entities.Card;
import java.time.LocalDate;

@Getter
public class CardResponse {
    private Long id;
    private String fronte;
    private String retro;
    private Float ease;
    private Integer intervallo;
    private Integer ripetizioni;
    private LocalDate prossimaRevisione;
    private LocalDate ultimaRevisione;

    public CardResponse(Card card) {
        this.id = card.getId();
        this.fronte = card.getFronte();
        this.retro = card.getRetro();
        this.ease = card.getEase();
        this.intervallo = card.getIntervallo();
        this.ripetizioni = card.getRipetizioni();
        this.prossimaRevisione = card.getProssimaRevisione();
        this.ultimaRevisione = card.getUltimaRevisione();
    }
}


