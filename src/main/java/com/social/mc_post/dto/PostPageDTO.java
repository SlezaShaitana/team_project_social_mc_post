package com.social.mc_post.dto;

import com.social.mc_post.model.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostPageDTO {
    private long totalElements;
    private int totalPages;
    private List sort;
    private int numberOfElements;
    private PageableDTO pageable;
    private boolean first;
    private boolean last;
    private int size;
    private List<PostDto> content;
    private int number;
    private boolean empty;
}
