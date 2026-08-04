package noemicoppotelli.ariadne.repositories;
import noemicoppotelli.ariadne.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUtenteId(Long utenteId);
    List<Task> findByUtenteIdAndCompletato(Long utenteId, Boolean completato);

}
