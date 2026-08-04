package noemicoppotelli.ariadne.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import noemicoppotelli.ariadne.entities.MappaConcettuale;
import java.util.List;

public interface MappaConcettualeRepository extends JpaRepository<MappaConcettuale, Long> {
    List<MappaConcettuale> findByUtenteId(Long utenteId);
}