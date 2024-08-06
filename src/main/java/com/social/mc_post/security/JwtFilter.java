package com.social.mc_post.security;

import com.social.mc_post.exception.AuthException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtValidation jwtValidation;

    @Autowired
    public JwtFilter(JwtValidation jwtValidation) {
        this.jwtValidation = jwtValidation;
    }

    public String getToken(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        return (token.startsWith("Bearer ") && token != null) ? token.substring(7) : null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String tokenAuth = getToken(request);
        if (tokenAuth != null && getToken()){
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("USER"));
            Authentication authentication = new UsernamePasswordAuthenticationToken(tokenAuth, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }else {
            throw new AuthException("Jwt token not validate.");
        }
        filterChain.doFilter(request, response);
    }

    protected Boolean getToken(){
        return true;
    }
}
