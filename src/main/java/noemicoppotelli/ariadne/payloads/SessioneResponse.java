package noemicoppotelli.ariadne.payloads;
import lombok.Getter;
import noemicoppotelli.ariadne.entities.Sessione;

import java.time.LocalDateTime;

@Getter
public class SessioneResponse {
    private Long id;
    private String materia;
    private Integer minuti;
    private LocalDateTime iniziata;
    private LocalDateTime completata;

    public SessioneResponse(Sessione sessione) {
        this.id = sessione.getId();
        this.materia = sessione.getMateria();
        this.minuti = sessione.getMinuti();
        this.iniziata = sessione.getIniziata();
        this.completata = sessione.getCompletata();
    }
}