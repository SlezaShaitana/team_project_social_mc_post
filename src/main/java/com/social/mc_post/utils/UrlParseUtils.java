package com.social.mc_post.utils;


import com.social.mc_post.dto.PageDto;
import com.social.mc_post.dto.PostSearchDto;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class UrlParseUtils {

    public static PageDto getPageable(String url) {
        PageDto pageDto = new PageDto();

        if (url.contains("page%3D")){
            int startIndex = url.indexOf("page%3D");
            startIndex += "page%3D".length();
            int endIndex = url.indexOf("&", startIndex);

            if (endIndex == -1) {
                endIndex = url.length();
            }

            pageDto.setPage(Integer.parseInt(url.substring(startIndex, endIndex)));
        }

        if (url.contains("size%3D")){
            int startIndex = url.indexOf("size%3D");
            startIndex += "size%3D".length();
            int endIndex = url.indexOf("&", startIndex);

            if (endIndex == -1) {
                endIndex = url.length();
            }

            pageDto.setSize(Integer.parseInt(url.substring(startIndex, endIndex)));
        }

        return pageDto;
    }

    public static PostSearchDto getSearchDTO(String url) {
        PostSearchDto searchDTO = new PostSearchDto();

        if (url.contains("author%3D")) {
            int startIndex = url.indexOf("author%3D");
            startIndex += "author%3D".length();
            int endIndex = url.indexOf("&", startIndex);

            if (endIndex == -1) {
                endIndex = url.length();
            }

            String author = url.substring(startIndex, endIndex);
            author = URLDecoder.decode(author, StandardCharsets.UTF_8);

            searchDTO.setAuthor(author);
        }

        return searchDTO;
    }
}
