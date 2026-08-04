package noemicoppotelli.ariadne.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import noemicoppotelli.ariadne.entities.Card;
import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByDeckId(Long deckId);
}
