package com.social.mc_post.repository.specifications;

import com.social.mc_post.structure.PostEntity;
import org.springframework.data.jpa.domain.Specification;

public class PostSpecification {

    public static Specification<PostEntity> getTitle(String title){
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("title"), String.format("%%%s%%", title));
    }
}
