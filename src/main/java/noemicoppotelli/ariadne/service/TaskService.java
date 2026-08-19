package noemicoppotelli.ariadne.service;
import noemicoppotelli.ariadne.entities.Task;
import noemicoppotelli.ariadne.entities.Utente;
import noemicoppotelli.ariadne.payloads.TaskResponse;
import noemicoppotelli.ariadne.repositories.TaskRepository;
import noemicoppotelli.ariadne.payloads.TaskRequest;
import lombok.RequiredArgsConstructor;
import noemicoppotelli.ariadne.exceptions.NotFoundException;
import noemicoppotelli.ariadne.exceptions.UnauthorizedException;
import org.springframework.stereotype.Service;
import java.util.List;



@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskResponse creaTask(TaskRequest request, Utente utente) {
        Task task = new Task();
        task.setTitolo(request.getTitolo());
        task.setMateria(request.getMateria());
        task.setUtente(utente);
        task.setScadenza(request.getScadenza());
        task.setPriorita(request.getPriorita());
        Task salvata = taskRepository.save(task);
        return new TaskResponse(salvata);
    }

    public List<TaskResponse> getTaskByUtente(Utente utente) {
        return taskRepository.findByUtenteId(utente.getId()).stream()
                .map(TaskResponse::new)
                .toList();
    }

    private Task getTaskEDaVerificare(Long id, Utente utente) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task non trovata"));
        if (!task.getUtente().getId().equals(utente.getId())) {
            throw new UnauthorizedException("Questa task non appartiene a te.");
        }
        return task;
    }

    public TaskResponse aggiornaTask(Long id, TaskRequest request, Utente utente) {
        Task task = getTaskEDaVerificare(id, utente);
        task.setTitolo(request.getTitolo());
        task.setMateria(request.getMateria());
        task.setScadenza(request.getScadenza());
        task.setPriorita(request.getPriorita());
        Task salvata = taskRepository.save(task);
        return new TaskResponse(salvata);
    }
    public TaskResponse toggleCompletato(Long id, Utente utente) {
        Task task = getTaskEDaVerificare(id, utente);
        task.setCompletato(!task.isCompletato());
        Task salvata = taskRepository.save(task);
        return new TaskResponse(salvata);
    }

    public void eliminaTask(Long id, Utente utente) {
        Task task = getTaskEDaVerificare(id, utente);
        taskRepository.delete(task);
    }
}

