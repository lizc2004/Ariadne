package noemicoppotelli.ariadne.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import noemicoppotelli.ariadne.entities.Sessione;
import java.time.LocalDateTime;
import java.util.List;

public interface SessioneRepository extends JpaRepository<Sessione, Long> {
    List<Sessione> findByUtenteId(Long utenteId);
    List<Sessione> findByUtenteIdAndIniziataBetween(Long utenteId, LocalDateTime da, LocalDateTime a);
}