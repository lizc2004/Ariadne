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

public UtenteService(UtenteRepository utenteRepository, PasswordEncoder passwordEncoder,
                     AuthenticationManager authenticationManager, JwtService jwtService) {
    this.utenteRepository = utenteRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
}
public LoginResponse login(LoginRequest request) {
    try {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
    } catch (AuthenticationException e) {
        throw new UnauthorizedException("Credenziali non valide.");
    }

    String token = jwtService.generateToken(request.getEmail());
    return new LoginResponse(token);
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
