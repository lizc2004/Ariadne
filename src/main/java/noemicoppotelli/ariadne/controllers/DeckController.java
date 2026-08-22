package noemicoppotelli.ariadne.controllers;
import noemicoppotelli.ariadne.entities.Utente;
import noemicoppotelli.ariadne.exceptions.UnauthorizedException;
import noemicoppotelli.ariadne.payloads.DeckRequest;
import noemicoppotelli.ariadne.payloads.DeckResponse;
import noemicoppotelli.ariadne.repositories.UtenteRepository;
import noemicoppotelli.ariadne.service.DeckService;
import noemicoppotelli.ariadne.service.CardService;
import noemicoppotelli.ariadne.payloads.CardRequest;
import noemicoppotelli.ariadne.payloads.CardResponse;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/decks")
@RequiredArgsConstructor
public class DeckController {
    private final DeckService deckService;
    private final CardService cardService;
    private final UtenteRepository utenteRepository;

    private Utente getUtenteAutenticato(UserDetails userDetails) {
        return utenteRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Utente non trovato."));
    }
    @PostMapping
    public ResponseEntity<DeckResponse> creaDeck(@Valid @RequestBody DeckRequest request,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        DeckResponse deck = deckService.createDeck(request, getUtenteAutenticato(userDetails));
        return ResponseEntity.status(HttpStatus.CREATED).body(deck);
    }

    @GetMapping
    public ResponseEntity<List<DeckResponse>> getDecks(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(deckService.getDecks(getUtenteAutenticato(userDetails)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeckResponse> aggiornaDeck(@PathVariable Long id, @Valid @RequestBody DeckRequest request,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        DeckResponse deck = deckService.aggiornaDeck(id, request, getUtenteAutenticato(userDetails));
        return ResponseEntity.ok(deck);
    }

    @PostMapping("/{deckId}/cards")
    public ResponseEntity<CardResponse> creaCard(@PathVariable Long deckId,
                                                 @Valid @RequestBody CardRequest request,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        CardResponse card = cardService.creaCard(deckId, request, getUtenteAutenticato(userDetails));
        return ResponseEntity.status(HttpStatus.CREATED).body(card);
    }

    @GetMapping("/{deckId}/cards")
    public ResponseEntity<List<CardResponse>> getCardsByDeck(@PathVariable Long deckId,
                                                             @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(cardService.getCardsByDeck(deckId, getUtenteAutenticato(userDetails)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminaDeck(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        deckService.eliminaDeck(id, getUtenteAutenticato(userDetails));
        return ResponseEntity.noContent().build();
    }
}
