package noemicoppotelli.ariadne.payloads;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import noemicoppotelli.ariadne.enums.Valutazione;

@Getter
@Setter
public class ValutazioneRequest {
    @NotNull
    private Valutazione valutazione;
}
