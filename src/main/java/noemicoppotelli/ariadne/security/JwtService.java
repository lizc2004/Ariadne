package noemicoppotelli.ariadne.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import javax.crypto.SecretKey;

import java.security.Key;
import java.util.Date;

@Service

public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private SecretKey getKey() {
        byte[] decodedKey = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(decodedKey);
    }


    public String generateToken(String mail) {
        return Jwts.builder()
                .subject(mail)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey())
                .compact();
    }


    public String getMailFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isTokenValido(String token, UserDetails userDetails) {
        String mail = getMailFromToken(token);
        return mail != null && mail.equals(userDetails.getUsername());
    }
}


