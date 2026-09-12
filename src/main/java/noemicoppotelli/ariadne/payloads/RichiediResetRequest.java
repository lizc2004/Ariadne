package noemicoppotelli.ariadne.payloads;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RichiediResetRequest {
    @NotBlank
    @Email
    private String email;
}
