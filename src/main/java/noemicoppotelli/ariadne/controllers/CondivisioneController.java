package noemicoppotelli.ariadne.controllers;
import noemicoppotelli.ariadne.entities.Utente;
import noemicoppotelli.ariadne.exceptions.UnauthorizedException;
import noemicoppotelli.ariadne.payloads.CondivisioneRequest;
import noemicoppotelli.ariadne.payloads.CondivisioneResponse;
import noemicoppotelli.ariadne.repositories.UtenteRepository;
import noemicoppotelli.ariadne.service.CondivisioneService;
import noemicoppotelli.ariadne.payloads.ProgressiResponse;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;


@RestController
@RequestMapping("/api/condivisioni")
@RequiredArgsConstructor
public class CondivisioneController {
    private final CondivisioneService condivisioneService;
    private final UtenteRepository utenteRepository;

    private Utente getUtenteAutenticato(UserDetails userDetails) {
        return utenteRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Utente non trovato."));
    }

    @PostMapping
    public ResponseEntity<CondivisioneResponse> richiediCondivisione(@Valid @RequestBody CondivisioneRequest request,
                                                                     @AuthenticationPrincipal UserDetails userDetails) {
        CondivisioneResponse response = condivisioneService.richiediCondivisione(request, getUtenteAutenticato(userDetails));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/accetta")
    public ResponseEntity<CondivisioneResponse> accettaCondivisione(@PathVariable Long id,
                                                                    @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(condivisioneService.accettaCondivisione(id, getUtenteAutenticato(userDetails)));
    }

    @PatchMapping("/{id}/rifiuta")
    public ResponseEntity<CondivisioneResponse> rifiutaCondivisione(@PathVariable Long id,
                                                                    @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(condivisioneService.rifiutaCondivisione(id, getUtenteAutenticato(userDetails)));
    }

    @PatchMapping("/{id}/revoca")
    public ResponseEntity<CondivisioneResponse> revocaCondivisione(@PathVariable Long id,
                                                                   @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(condivisioneService.revocaCondivisione(id, getUtenteAutenticato(userDetails)));
    }

    @GetMapping("/ricevute")
    public ResponseEntity<List<CondivisioneResponse>> getRicevute(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(condivisioneService.getRicevute(getUtenteAutenticato(userDetails)));
    }

    @GetMapping("/concesse")
    public ResponseEntity<List<CondivisioneResponse>> getConcesse(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(condivisioneService.getConcesse(getUtenteAutenticato(userDetails)));
    }
    @GetMapping("/{id}/progressi")
    public ResponseEntity<ProgressiResponse> getProgressi(@PathVariable Long id,
                                                          @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(condivisioneService.getProgressi(id, getUtenteAutenticato(userDetails)));
    }
}