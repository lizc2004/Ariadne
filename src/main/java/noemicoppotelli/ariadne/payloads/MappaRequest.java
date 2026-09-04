package noemicoppotelli.ariadne.payloads;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MappaRequest {
    @NotBlank
    @Size(max = 4000)
    private String testo;
}
