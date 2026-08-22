package noemicoppotelli.ariadne.payloads;
import lombok.Getter;
import noemicoppotelli.ariadne.entities.Deck;

@Getter
public class DeckResponse {
    private Long id;
    private String nome;

    public DeckResponse(Deck deck) {
        this.id = deck.getId();
        this.nome = deck.getNome();
    }
}
