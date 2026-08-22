package noemicoppotelli.ariadne.controllers;
import noemicoppotelli.ariadne.entities.Utente;
import noemicoppotelli.ariadne.exceptions.UnauthorizedException;
import noemicoppotelli.ariadne.payloads.CardResponse;
import noemicoppotelli.ariadne.payloads.CardRequest;
import noemicoppotelli.ariadne.service.CardService;
import noemicoppotelli.ariadne.payloads.ValutazioneRequest;
import noemicoppotelli.ariadne.repositories.UtenteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;
    private final UtenteRepository utenteRepository;

    private Utente getUtenteAutenticato(UserDetails userDetails) {
        return utenteRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Utente non trovato."));
    }


    @PutMapping("/{id}")
    public ResponseEntity<CardResponse> updateCard(@PathVariable Long id,
                                                    @Valid @RequestBody CardRequest request,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        CardResponse card = cardService.aggiornaCard(id, request, getUtenteAutenticato(userDetails));
        return ResponseEntity.ok(card);
    }
    @PatchMapping("/{id}/valuta")
    public ResponseEntity<CardResponse> valutaCarta(@PathVariable Long id,
                                                    @Valid @RequestBody ValutazioneRequest request,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        CardResponse card = cardService.valutaCarta(id, getUtenteAutenticato(userDetails), request.getValutazione());
        return ResponseEntity.ok(card);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        cardService.eliminaCard(id, getUtenteAutenticato(userDetails));
        return ResponseEntity.noContent().build();
    }
}
