package noemicoppotelli.ariadne.payloads;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CondivisioneRequest {
    @NotBlank
    @Email
    private String emailOwner;
}
