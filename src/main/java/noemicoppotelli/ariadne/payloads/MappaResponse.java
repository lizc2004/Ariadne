package noemicoppotelli.ariadne.payloads;
import lombok.Getter;
import noemicoppotelli.ariadne.entities.MappaConcettuale;

import java.time.LocalDateTime;

@Getter
public class MappaResponse {
    private final Long id;
    private final String contenutoMermaid;
    private final LocalDateTime createdAt;

    public MappaResponse(MappaConcettuale mappa) {
        this.id = mappa.getId();
        this.contenutoMermaid = mappa.getContenutoMermaid();
        this.createdAt = mappa.getCreatedAt();
    }
}
