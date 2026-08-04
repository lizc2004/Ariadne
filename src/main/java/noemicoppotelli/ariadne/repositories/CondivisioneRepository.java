package noemicoppotelli.ariadne.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import noemicoppotelli.ariadne.entities.Condivisione;
import java.util.List;

public interface CondivisioneRepository extends JpaRepository<Condivisione, Long> {
    List<Condivisione> findByOwnerId(Long ownerId);
    List<Condivisione> findByViewerId(Long viewerId);
}
