package org.izomp.transaction.manager.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.izomp.transaction.manager.config.AppProperties;
import org.izomp.transaction.manager.security.model.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class TokenProvider {

    private AppProperties appProperties;

    public TokenProvider(AppProperties appProperties) {
        this.appProperties = appProperties;
    }


    public String createToken(Authentication authentication) {
        return createToken((UserPrincipal) authentication.getPrincipal());
    }


    public String createToken(UserPrincipal userPrincipal) {
        return createToken(userPrincipal.getId());
    }

    public String createToken(UUID id) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());

        return Jwts.builder()
                .setSubject(id.toString())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, appProperties.getAuth().getTokenSecret())
                .compact();
    }

    public UUID getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(appProperties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody();

        return UUID.fromString(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(appProperties.getAuth().getTokenSecret())
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

    public static void main(String[] args) {
        UUID id = UUID.randomUUID();
        AppProperties appProperties = new AppProperties();
        appProperties.getAuth().setTokenExpirationMsec(864000000);
        appProperties.getAuth().setTokenSecret("926D96C90030DD58429D2751AC1BDBBC");
        TokenProvider provider = new TokenProvider(appProperties);
        String token = provider.createToken(id);
        System.out.println(provider.validateToken(token));
    }
}
