package com.social.mc_post.security;

import com.social.mc_post.aop.JwtTokenException;
import com.social.mc_post.feign.JwtValidation;
import io.jsonwebtoken.MalformedJwtException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtValidation jwtValidation;

    private String getToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        log.error("Request is empty or damaged");
        throw new IllegalArgumentException("Authorization header is missing or malformed");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if (requestURI.equals("/prometheus") || requestURI.equals("/actuator/prometheus")) {
            log.info("Skipping JWT validation for URI: {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String stringToken = getToken(request);
            if (jwtValidation.validateToken(stringToken)) {
                DecodedToken token = DecodedToken.getDecoded(stringToken);
                String email = token.getEmail();
                List<String> roles = token.getRole();

                Collection<? extends GrantedAuthority> authorities = (roles != null ? roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList()) : Collections.emptyList());

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        email, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token format: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } catch (Exception e) {
            log.error("JWT token validation failed: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }

}