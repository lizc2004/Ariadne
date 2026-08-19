package noemicoppotelli.ariadne.controllers;
import noemicoppotelli.ariadne.entities.Utente;
import noemicoppotelli.ariadne.exceptions.UnauthorizedException;
import noemicoppotelli.ariadne.payloads.TaskRequest;
import noemicoppotelli.ariadne.payloads.TaskResponse;
import noemicoppotelli.ariadne.repositories.UtenteRepository;
import noemicoppotelli.ariadne.service.TaskService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final UtenteRepository utenteRepository;

    private Utente getUtenteAutenticato(UserDetails userDetails) {
        return utenteRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Utente non trovato."));
    }

    @PostMapping
    public ResponseEntity<TaskResponse> creaTask(@Valid @RequestBody TaskRequest request,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        TaskResponse task = taskService.creaTask(request, getUtenteAutenticato(userDetails));
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(taskService.getTaskByUtente(getUtenteAutenticato(userDetails)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> aggiornaTask(@PathVariable Long id, @Valid @RequestBody TaskRequest request,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(taskService.aggiornaTask(id, request, getUtenteAutenticato(userDetails)));
    }

    @PatchMapping("/{id}/completa")
    public ResponseEntity<TaskResponse> toggleCompletato(@PathVariable Long id,
                                                         @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(taskService.toggleCompletato(id, getUtenteAutenticato(userDetails)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminaTask(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        taskService.eliminaTask(id, getUtenteAutenticato(userDetails));
        return ResponseEntity.noContent().build();
    }
}
