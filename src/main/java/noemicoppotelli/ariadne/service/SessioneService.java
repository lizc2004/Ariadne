package noemicoppotelli.ariadne.service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import noemicoppotelli.ariadne.entities.Sessione;
import noemicoppotelli.ariadne.entities.Utente;
import noemicoppotelli.ariadne.payloads.SessioneRequest;
import noemicoppotelli.ariadne.payloads.SessioneResponse;
import noemicoppotelli.ariadne.repositories.SessioneRepository;
import noemicoppotelli.ariadne.exceptions.NotFoundException;
import noemicoppotelli.ariadne.exceptions.UnauthorizedException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessioneService {

    private final SessioneRepository sessioneRepository;

    public SessioneResponse iniziaSessione(SessioneRequest request, Utente utente) {
        Sessione sessione = new Sessione();
        sessione.setMateria(request.getMateria());
        sessione.setIniziata(LocalDateTime.now());
        sessione.setMinuti(0);
        sessione.setUtente(utente);
        Sessione salvata = sessioneRepository.save(sessione);
        return new SessioneResponse(salvata);
    }

    private Sessione getSessioneEDaVerificare(Long id, Utente utente) {
        Sessione sessione = sessioneRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sessione non trovata"));
        if (!sessione.getUtente().getId().equals(utente.getId())) {
            throw new UnauthorizedException("Questa sessione non appartiene a te.");
        }
        return sessione;
    }

    public SessioneResponse completaSessione(Long id, Utente utente) {
        Sessione sessione = getSessioneEDaVerificare(id, utente);
        sessione.setCompletata(LocalDateTime.now());
        long minuti = ChronoUnit.MINUTES.between(sessione.getIniziata(), sessione.getCompletata());
        sessione.setMinuti((int) minuti);
        Sessione salvata = sessioneRepository.save(sessione);
        return new SessioneResponse(salvata);
    }

    public List<SessioneResponse> getSessioniInIntervallo(Utente utente, LocalDateTime da, LocalDateTime a) {
        return sessioneRepository.findByUtenteIdAndIniziataBetween(utente.getId(), da, a).stream()
                .map(SessioneResponse::new)
                .toList();
    }
}
