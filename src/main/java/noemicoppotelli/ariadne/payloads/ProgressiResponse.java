package noemicoppotelli.ariadne.payloads;
import lombok.Getter;
import lombok.AllArgsConstructor;
import java.util.List;

@Getter
@AllArgsConstructor
public class ProgressiResponse {
    private List<TaskResponse> taskInScadenza;
    private int carteDaRipassare;
    private List<SessioneResponse> sessioni;
}