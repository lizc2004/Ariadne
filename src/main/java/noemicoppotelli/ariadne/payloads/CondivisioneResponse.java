package noemicoppotelli.ariadne.payloads;
import lombok.Getter;
import noemicoppotelli.ariadne.entities.Condivisione;
import noemicoppotelli.ariadne.enums.StatoCondivisione;

import java.time.LocalDateTime;

@Getter
public class CondivisioneResponse {
    private Long id;
    private String emailOwner;
    private String emailViewer;
    private StatoCondivisione stato;
    private LocalDateTime createdAt;

    public CondivisioneResponse(Condivisione condivisione) {
        this.id = condivisione.getId();
        this.emailOwner = condivisione.getOwner().getEmail();
        this.emailViewer = condivisione.getViewer().getEmail();
        this.stato = condivisione.getStato();
        this.createdAt = condivisione.getCreatedAt();
    }
}
