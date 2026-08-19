package noemicoppotelli.ariadne.payloads;
import lombok.Getter;
import noemicoppotelli.ariadne.entities.Task;
import noemicoppotelli.ariadne.enums.Priorita;

import java.time.LocalDate;

@Getter
public class TaskResponse {
    private Long id;
    private String titolo;
    private String materia;
    private LocalDate scadenza;
    private Priorita priorita;
    private boolean completato;

    public TaskResponse(Task task) {
        this.id = task.getId();
        this.titolo = task.getTitolo();
        this.materia = task.getMateria();
        this.scadenza = task.getScadenza();
        this.priorita = task.getPriorita();
        this.completato = task.isCompletato();
    }
}