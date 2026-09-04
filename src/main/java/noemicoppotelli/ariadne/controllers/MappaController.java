package noemicoppotelli.ariadne.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import noemicoppotelli.ariadne.entities.Utente;
import noemicoppotelli.ariadne.payloads.MappaRequest;
import noemicoppotelli.ariadne.payloads.MappaResponse;
import noemicoppotelli.ariadne.repositories.UtenteRepository;
import noemicoppotelli.ariadne.service.MappaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * BOZZA DIDATTICA — non registrata/usata in produzione in questa versione.
 */
@RestController
@RequestMapping("/api/mappe")
@RequiredArgsConstructor
public class MappaController {
    private final MappaService mappaService;
    private final UtenteRepository utenteRepository;

    @PostMapping("/genera")
    public ResponseEntity<MappaResponse> genera(@Valid @RequestBody MappaRequest request,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        Utente utente = getUtenteAutenticato(userDetails);
        MappaResponse response = mappaService.genera(request.getTesto(), utente);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private Utente getUtenteAutenticato(UserDetails userDetails) {
        return utenteRepository.findByEmail(userDetails.getUsername())
                .orElseThrow();
    }
}
