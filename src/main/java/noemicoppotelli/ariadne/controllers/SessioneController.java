package noemicoppotelli.ariadne.controllers;
import noemicoppotelli.ariadne.entities.Utente;
import noemicoppotelli.ariadne.exceptions.UnauthorizedException;
import noemicoppotelli.ariadne.payloads.SessioneRequest;
import noemicoppotelli.ariadne.payloads.SessioneResponse;
import noemicoppotelli.ariadne.repositories.UtenteRepository;
import noemicoppotelli.ariadne.service.SessioneService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/sessioni")
@RequiredArgsConstructor
public class SessioneController {
    private final SessioneService sessioneService;
    private final UtenteRepository utenteRepository;

    private Utente getUtenteAutenticato(UserDetails userDetails) {
        return utenteRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Utente non trovato."));
    }

    @PostMapping
    public ResponseEntity<SessioneResponse> iniziaSessione(@Valid @RequestBody SessioneRequest request,
                                                           @AuthenticationPrincipal UserDetails userDetails) {
        SessioneResponse sessioneResponse = sessioneService.iniziaSessione(request, getUtenteAutenticato(userDetails));
        return new ResponseEntity<>(sessioneResponse, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/completa")
    public ResponseEntity<SessioneResponse> completaSessione(@PathVariable Long id,
                                                             @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(sessioneService.completaSessione(id, getUtenteAutenticato(userDetails)));
    }

    @GetMapping
    public ResponseEntity<List<SessioneResponse>> getSessioniInIntervallo(@RequestParam LocalDateTime da,
                                                                           @RequestParam LocalDateTime a,
                                                                           @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(sessioneService.getSessioniInIntervallo(getUtenteAutenticato(userDetails), da, a));
    }
}
