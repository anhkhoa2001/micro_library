package org.micro.config.token;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.micro.dto.UserDetailCustomize;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtTokenSetup {

    @Value("${secret.key}")
    private String SECRET_KEY;

    @Value("${secret.timer}")
    private long TIMER;

    public JwtTokenSetup() {
    }

    public String generateToken(UserDetailCustomize user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + TIMER);

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public String getUsernamFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Sai token");
        } catch (ExpiredJwtException ex) {
            log.error("Token đã hết hạn");
        }

        return false;
    }
}
