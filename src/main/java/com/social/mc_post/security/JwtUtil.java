package com.social.mc_post.security;


import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class JwtUtil {
    public String getIdFromToken(String token){
        return Jwts.parser().build()
                .parseSignedClaims(token).getPayload().getId();
    }

    public String getEmailFromToken(String token){
        return Jwts.parser().build()
                .parseSignedClaims(token).getPayload().get("email", String.class);
    }

    public List<String> getRolesFromToken(String token){
        return Jwts.parser().build()
                .parseSignedClaims(token).getPayload().get("roles", List.class);
    }
}
