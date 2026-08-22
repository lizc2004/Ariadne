package noemicoppotelli.ariadne.payloads;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardRequest {
    @NotBlank
    private String fronte;

    @NotBlank
    private String retro;
}
