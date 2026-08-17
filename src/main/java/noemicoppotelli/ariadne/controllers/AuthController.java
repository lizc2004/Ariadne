package noemicoppotelli.ariadne.controllers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import noemicoppotelli.ariadne.payloads.RegisterRequest;
import noemicoppotelli.ariadne.payloads.LoginRequest;
import noemicoppotelli.ariadne.payloads.LoginResponse;
import noemicoppotelli.ariadne.service.UtenteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UtenteService utenteService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        utenteService.registra(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = utenteService.login(request);
        return ResponseEntity.ok(response);
    }

}
