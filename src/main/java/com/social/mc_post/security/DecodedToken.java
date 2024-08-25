package com.social.mc_post.security;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Getter
public class DecodedToken {

    private String id;
    private String email;
    private List<String> roles;

    public static DecodedToken getDecoded(String encodedToken) throws UnsupportedEncodingException {
        String[] pieces = encodedToken.split("\\.");
        String b64payload = pieces[1];
        String jsonString = new String(Base64.decodeBase64(b64payload), "UTF-8");

        return new Gson().fromJson(jsonString, DecodedToken.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

}
