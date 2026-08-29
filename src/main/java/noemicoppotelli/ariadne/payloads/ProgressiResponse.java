package noemicoppotelli.ariadne.payloads;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class ProgressiResponse {
    private int taskTotali;
    private int taskCompletate;
    private int minutiStudioUltimi7Giorni;
}