package noemicoppotelli.ariadne.service;
import noemicoppotelli.ariadne.entities.Deck;
import noemicoppotelli.ariadne.entities.Utente;
import noemicoppotelli.ariadne.payloads.DeckResponse;
import noemicoppotelli.ariadne.repositories.DeckRepository;
import noemicoppotelli.ariadne.payloads.DeckRequest;
import lombok.RequiredArgsConstructor;
import noemicoppotelli.ariadne.exceptions.NotFoundException;
import noemicoppotelli.ariadne.exceptions.UnauthorizedException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeckService {
    private final DeckRepository deckRepository;

    public DeckResponse createDeck(DeckRequest deckRequest, Utente utente) {
        Deck deck = new Deck();
        deck.setNome(deckRequest.getNome());
        deck.setUtente(utente);
        deckRepository.save(deck);
        return new DeckResponse(deck);
    }

    public List<DeckResponse> getDecks(Utente utente){
        return deckRepository.findByUtenteId(utente.getId()).stream()
                .map(DeckResponse::new)
                .toList();
    }
    private Deck getDeckEDaVerificare(Long id, Utente utente) {
        Deck deck = deckRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Deck non trovata"));
        if (!deck.getUtente().getId().equals(utente.getId())) {
            throw new UnauthorizedException("Non sei autorizzato a verificare questo deck");
        }
        return deck;
    }

    public DeckResponse aggiornaDeck(Long id, DeckRequest deckRequest, Utente utente) {
        Deck deck = getDeckEDaVerificare(id, utente);
        deck.setNome(deckRequest.getNome());
        deckRepository.save(deck);
        return new DeckResponse(deck);
    }

    public void eliminaDeck(Long id, Utente utente) {
        Deck deck = getDeckEDaVerificare(id, utente);
        deckRepository.delete(deck);
    }
}
