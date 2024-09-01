package com.social.mc_post.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.List;

@Data
public class PageDto {
    @Min(value = 0, message = "The page value must be greater than or equal to 0.")
    private int page;

    @Min(value = 1, message = "the page value must be greater than or equal to 1.")
    private int size;

    private List<String> sort;
}