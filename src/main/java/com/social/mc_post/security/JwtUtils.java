package com.social.mc_post.security;


import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Slf4j
public class JwtUtils {

    @Value("${app.jwt.secret}")
    private String secret;

    public String getId(String token){
        return Jwts.parser().verifyWith(createSecretKey(secret)).build()
                .parseSignedClaims(token).getPayload().getId();

    }

    public String getEmail(String token){
        return Jwts.parser().verifyWith(createSecretKey(secret))
                .build().parseSignedClaims(token).getPayload().get("email", String.class);

    }

    public List<String> getRoles(String token){
        return Jwts.parser().verifyWith(createSecretKey(secret))
                .build().parseSignedClaims(token).getPayload().get("roles", List.class);

    }

    public static SecretKey createSecretKey(String secret) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256");
    }

}

