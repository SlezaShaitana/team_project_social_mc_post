package com.social.mc_post.security;

import com.social.mc_post.exception.AuthException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtValidation jwtValidation;
    private final JwtUtil jwtUtil;

    @Autowired
    public JwtFilter(JwtValidation jwtValidation, JwtUtil jwtUtil) {
        this.jwtValidation = jwtValidation;
        this.jwtUtil = jwtUtil;
    }

    public String getToken(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        return (token.startsWith("Bearer ") && token != null) ? token.substring(7) : null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String tokenAuth = getToken(request);
        if (tokenAuth != null && jwtValidation.validateToken(tokenAuth)){

            String emailUser = jwtUtil.getEmailFromToken(tokenAuth);
            List<String> roles = jwtUtil.getRolesFromToken(tokenAuth);

            Collection<? extends GrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    emailUser, null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }else {
            throw new AuthException("Jwt token not validate.");
        }
        filterChain.doFilter(request, response);
    }
}
