package com.social.mc_post.security;

import com.social.mc_post.dto.UserShortDto;
import com.social.mc_post.exception.AuthException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    @Getter
    public UUID userId;

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
        if (tokenAuth != null && getToken()){
            UserShortDto userShortDto = jwtUtil.mapToUserShortDto(tokenAuth);
            Collection<? extends GrantedAuthority> authorities = userShortDto.getRoles().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userShortDto.getEmail(), userShortDto.getUserId(), authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            userId = UUID.fromString(userShortDto.getUserId());

        }else {
            throw new AuthException("Jwt token not validate.");
        }
        filterChain.doFilter(request, response);
    }

    protected Boolean getToken(){
        return true;
    }
}
