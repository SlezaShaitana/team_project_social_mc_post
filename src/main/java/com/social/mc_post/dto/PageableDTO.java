package com.social.mc_post.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageableDTO {
    private List sort;
    private int pageNumber;
    private boolean unpaged;
    private boolean paged;
    private int pageSize;
    private int offset;
}
