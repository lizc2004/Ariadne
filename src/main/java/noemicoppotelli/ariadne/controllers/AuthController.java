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
import noemicoppotelli.ariadne.payloads.RefreshTokenRequest;
import noemicoppotelli.ariadne.entities.Utente;
import noemicoppotelli.ariadne.service.RefreshTokenService;
import noemicoppotelli.ariadne.security.JwtService;
import noemicoppotelli.ariadne.payloads.RichiediResetRequest;
import noemicoppotelli.ariadne.payloads.ResetPasswordRequest;
import noemicoppotelli.ariadne.service.PasswordResetService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UtenteService utenteService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final PasswordResetService passwordResetService;

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
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        Utente utente = refreshTokenService.validaRefreshToken(request.getRefreshToken());
        String nuovoAccessToken = jwtService.generateToken(utente.getEmail());
        return ResponseEntity.ok(new LoginResponse(nuovoAccessToken, request.getRefreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequest request) {
        refreshTokenService.revocaRefreshToken(request.getRefreshToken());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/richiedi-reset")
    public ResponseEntity<Void> richiediReset(@Valid @RequestBody RichiediResetRequest request) {
        passwordResetService.richiediReset(request.getEmail());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        passwordResetService.resettaPassword(request.getToken(), request.getNuovaPassword());
        return ResponseEntity.noContent().build();
    }

}
