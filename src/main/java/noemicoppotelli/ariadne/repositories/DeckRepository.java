package noemicoppotelli.ariadne.repositories;
import noemicoppotelli.ariadne.entities.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DeckRepository extends JpaRepository<Deck, Long> {
    List<Deck> findByUtenteId(Long utenteId);
}
