package noemicoppotelli.ariadne.service;
import noemicoppotelli.ariadne.repositories.UtenteRepository;
import noemicoppotelli.ariadne.repositories.CondivisioneRepository;
import noemicoppotelli.ariadne.payloads.CondivisioneResponse;
import noemicoppotelli.ariadne.entities.Condivisione;
import noemicoppotelli.ariadne.payloads.CondivisioneRequest;
import noemicoppotelli.ariadne.enums.StatoCondivisione;
import noemicoppotelli.ariadne.entities.Utente;
import noemicoppotelli.ariadne.exceptions.NotFoundException;
import noemicoppotelli.ariadne.exceptions.UnauthorizedException;
import noemicoppotelli.ariadne.exceptions.BadRequestException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class CondivisioneService {
    private final CondivisioneRepository condivisioneRepository;
    private final UtenteRepository utenteRepository;

    public CondivisioneService(CondivisioneRepository condivisioneRepository, UtenteRepository utenteRepository) {
        this.condivisioneRepository = condivisioneRepository;
        this.utenteRepository = utenteRepository;
    }


    public CondivisioneResponse richiediCondivisione(CondivisioneRequest request, Utente viewer) {
        Utente owner = utenteRepository.findByEmail(request.getEmailOwner())
                .orElseThrow(() -> new NotFoundException("Utente non trovato."));
        if (owner.getId().equals(viewer.getId())) {
            throw new BadRequestException("Non puoi richiedere accesso a te stesso.");
        }
        Condivisione condivisione = new Condivisione();
        condivisione.setOwner(owner);
        condivisione.setViewer(viewer);
        condivisione.setStato(StatoCondivisione.RICHIESTO);
        condivisione.setCreatedAt(LocalDateTime.now());
        Condivisione salvata = condivisioneRepository.save(condivisione);
        return new CondivisioneResponse(salvata);
    }

    private Condivisione getCondivisioneEDaVerificare(Long id, Utente utente) {
        Condivisione condivisione = condivisioneRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Condivisione non trovata"));
        if (!condivisione.getOwner().getId().equals(utente.getId())) {
            throw new UnauthorizedException("Solo il proprietario dei dati può gestire questa condivisione.");
        }
        return condivisione;
    }

    private CondivisioneResponse cambiaStato(Long id, Utente utente, StatoCondivisione nuovoStato) {
        Condivisione condivisione = getCondivisioneEDaVerificare(id, utente);
        condivisione.setStato(nuovoStato);
        return new CondivisioneResponse(condivisioneRepository.save(condivisione));
    }

    public CondivisioneResponse accettaCondivisione(Long id, Utente utente) {
        return cambiaStato(id, utente, StatoCondivisione.ACCETTATO);
    }

    public CondivisioneResponse rifiutaCondivisione(Long id, Utente utente) {
        return cambiaStato(id, utente, StatoCondivisione.RIFIUTATO);
    }

    public CondivisioneResponse revocaCondivisione(Long id, Utente utente) {
        return cambiaStato(id, utente, StatoCondivisione.REVOCATO);
    }

    public List<CondivisioneResponse> getRicevute(Utente utente) {
        return condivisioneRepository.findByOwnerId(utente.getId()).stream()
                .map(CondivisioneResponse::new)
                .toList();
    }

    public List<CondivisioneResponse> getConcesse(Utente utente) {
        return condivisioneRepository.findByViewerId(utente.getId()).stream()
                .filter(c -> c.getStato() == StatoCondivisione.ACCETTATO)
                .map(CondivisioneResponse::new)
                .toList();
    }
}
