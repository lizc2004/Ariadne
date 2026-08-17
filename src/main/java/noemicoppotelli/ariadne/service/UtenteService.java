package noemicoppotelli.ariadne.service;

import noemicoppotelli.ariadne.entities.Utente;
import noemicoppotelli.ariadne.repositories.UtenteRepository;
import noemicoppotelli.ariadne.exceptions.UnauthorizedException;
import noemicoppotelli.ariadne.exceptions.BadRequestException;
import noemicoppotelli.ariadne.payloads.RegisterRequest;
import noemicoppotelli.ariadne.payloads.LoginRequest;
import noemicoppotelli.ariadne.payloads.LoginResponse;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import noemicoppotelli.ariadne.security.JwtService;
import java.time.LocalDateTime;

@Service
public class UtenteService {
private final UtenteRepository utenteRepository;
private final PasswordEncoder passwordEncoder;
private final AuthenticationManager authenticationManager;
private final JwtService jwtService;
private final RefreshTokenService refreshTokenService;

public UtenteService(UtenteRepository utenteRepository, PasswordEncoder passwordEncoder,
                     AuthenticationManager authenticationManager, JwtService jwtService,
                     RefreshTokenService refreshTokenService) {
    this.utenteRepository = utenteRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
    this.refreshTokenService = refreshTokenService;
}
    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new UnauthorizedException("Credenziali non valide.");
        }

        Utente utente = utenteRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Credenziali non valide."));

        String accessToken = jwtService.generateToken(utente.getEmail());
        String refreshToken = refreshTokenService.creaRefreshToken(utente);

        return new LoginResponse(accessToken, refreshToken);
    }
    public void registra(RegisterRequest request) {
        if (utenteRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email già in uso.");
        }

        Utente nuovoUtente = new Utente(null, request.getEmail(), null, LocalDateTime.now());
        nuovoUtente.setPassword(request.getPassword(), passwordEncoder);
        utenteRepository.save(nuovoUtente);
    }

}
