package com.social.mc_post.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.social.mc_post.dto.UserShortDto;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class JwtUtil {
    public UserShortDto mapToUserShortDto (String token) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(getPayLoad(token), UserShortDto.class);
    }

    private String getPayLoad(String token){
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        return new String(decoder.decode(chunks[1]));
    }
    public String getIdFromToken(String token) throws JsonProcessingException {
        return mapToUserShortDto(token).getId();
    }

}
