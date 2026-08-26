package noemicoppotelli.ariadne.service;
import java.time.LocalDate;
import java.util.List;
import noemicoppotelli.ariadne.entities.Deck;
import noemicoppotelli.ariadne.entities.Utente;
import noemicoppotelli.ariadne.exceptions.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import noemicoppotelli.ariadne.entities.Card;
import noemicoppotelli.ariadne.enums.Valutazione;
import noemicoppotelli.ariadne.exceptions.NotFoundException;
import noemicoppotelli.ariadne.payloads.CardRequest;
import noemicoppotelli.ariadne.payloads.CardResponse;
import noemicoppotelli.ariadne.repositories.CardRepository;
import noemicoppotelli.ariadne.repositories.DeckRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final DeckRepository deckRepository;

    public CardResponse creaCard(Long deckId, CardRequest request, Utente utente) {
        Deck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new NotFoundException("Deck non trovato"));
        if (!deck.getUtente().getId().equals(utente.getId())) {
            throw new UnauthorizedException("Questo mazzo non appartiene a te.");
        }
        Card card = new Card();
        card.setFronte(request.getFronte());
        card.setRetro(request.getRetro());
        card.setDeck(deck);
        card.setEase(2.5f);
        card.setIntervallo(0);
        card.setRipetizioni(0);
        card.setProssimaRevisione(LocalDate.now());
        Card salvata = cardRepository.save(card);
        return new CardResponse(salvata);
    }

    public List<CardResponse> getCardsByDeck(Long deckId, Utente utente) {
        Deck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new NotFoundException("Deck non trovato"));
        if (!deck.getUtente().getId().equals(utente.getId())) {
            throw new UnauthorizedException("Questo mazzo non appartiene a te.");
        }
        return cardRepository.findByDeckId(deckId).stream()
                .map(CardResponse::new)
                .toList();
    }

    private Card getCardEDaVerificare(Long id, Utente utente) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Carta non trovata"));
        if (!card.getDeck().getUtente().getId().equals(utente.getId())) {
            throw new UnauthorizedException("Questa carta non appartiene a te.");
        }
        return card;
    }

    public CardResponse valutaCarta(Long cardId, Utente utente, Valutazione valutazione) {
        Card card = getCardEDaVerificare(cardId, utente);

        int q = switch (valutazione) {
            case NON_RICORDO -> 0;
            case DIFFICILE -> 1;
            case GIUSTO -> 2;
            case FACILE -> 3;
        };

        // Applicazione logica SM-2
        int repetitions = card.getRipetizioni();
        int interval = card.getIntervallo();
        float ease = card.getEase();

        if (q ==0) {
            repetitions = 0;
            interval = 0;
        } else {
            repetitions++;

            if (repetitions == 1) {
                interval = 1;
            } else if (repetitions == 2) {
                interval = (q == 1) ? 2 : 3;
            } else {
                interval = Math.round(interval * ease);
            }

            float delta = (q == 1) ? -0.2f : (q == 2) ? 0f : 0.15f;
            ease = Math.max(1.3f, ease + delta);
        }

        card.setRipetizioni(repetitions);
        card.setIntervallo(interval);
        card.setEase(ease);
        card.setUltimaRevisione(LocalDate.now());
        card.setProssimaRevisione(LocalDate.now().plusDays(interval));

        return new CardResponse(cardRepository.save(card));
    }
    public CardResponse aggiornaCard(Long id, CardRequest request, Utente utente){
        Card card = getCardEDaVerificare( id, utente);
        card.setFronte(request.getFronte());
        card.setRetro(request.getRetro());
        Card salvata = cardRepository.save(card);
        return new CardResponse(salvata);

    }

    public List<CardResponse> creaCardInBlocco(Long deckId, String testo, Utente utente) {
        Deck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new NotFoundException("Deck non trovato"));
        if (!deck.getUtente().getId().equals(utente.getId())) {
            throw new UnauthorizedException("Questo mazzo non appartiene a te.");
        }

        List<Card> nuoveCarte = new ArrayList<>();
        for (String riga : testo.split("\n")) {
            String linea = riga.trim();
            if (linea.isEmpty() || linea.startsWith("#")) continue;

            int separatore = linea.indexOf('|');
            if (separatore == -1) separatore = linea.indexOf('\t');
            if (separatore == -1) continue; // riga non valida, salta

            String fronte = linea.substring(0, separatore).trim();
            String retro = linea.substring(separatore + 1).trim();
            if (fronte.isEmpty() || retro.isEmpty()) continue;

            Card card = new Card();
            card.setFronte(fronte);
            card.setRetro(retro);
            card.setDeck(deck);
            card.setEase(2.5f);
            card.setIntervallo(0);
            card.setRipetizioni(0);
            card.setProssimaRevisione(LocalDate.now());
            nuoveCarte.add(card);
        }

        return cardRepository.saveAll(nuoveCarte).stream()
                .map(CardResponse::new)
                .toList();
    }
    public void eliminaCard(Long id, Utente utente) {
        Card card = getCardEDaVerificare(id, utente);
        cardRepository.delete(card);
    }
}

