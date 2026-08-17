package noemicoppotelli.ariadne.payloads;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String refreshToken;
}