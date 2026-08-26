package noemicoppotelli.ariadne.payloads;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BulkCardRequest {
    @NotBlank
    private String testo;
}
