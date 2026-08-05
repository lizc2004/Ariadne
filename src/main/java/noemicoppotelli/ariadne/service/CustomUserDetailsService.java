package noemicoppotelli.ariadne.service;
import lombok.RequiredArgsConstructor;
import noemicoppotelli.ariadne.entities.Utente;
import org.springframework.security.core.userdetails.User;
import noemicoppotelli.ariadne.repositories.UtenteRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class CustomUserDetailsService implements UserDetailsService {

    private final UtenteRepository utenteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
    Utente utente = utenteRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("Utente con email " + username + " non trovato"));

        return User.builder()
                .username(utente.getEmail())
            .password(utente.getPassword())
            .authorities("")
        .build();
    }
}