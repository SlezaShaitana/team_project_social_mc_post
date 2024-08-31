package com.social.mc_post.services.impl;

import com.social.mc_post.exception.ResourceNotFoundException;
import com.social.mc_post.security.DecodedToken;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class GettingDataService {

    public static String getUserIdFromToken(String userToken){
        String stringToken = userToken.substring(7);
        DecodedToken decodedToken = null;
        try {
            decodedToken = DecodedToken.getDecoded(stringToken);
        } catch (UnsupportedEncodingException e) {
            throw new ResourceNotFoundException("Не валидный токен");
        }
        UUID idAuthor = UUID.fromString(decodedToken.getId());
        return idAuthor.toString();
    }
}
