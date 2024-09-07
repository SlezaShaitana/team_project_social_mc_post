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
            boolean validateToken = jwtValidation.validateToken(stringToken);
            log.info("Result token verification in mc-auth is {}", validateToken);
            if (validateToken) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        null, null, null
                );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            log.error("Error : {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

}