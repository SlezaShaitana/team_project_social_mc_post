package com.social.mc_post.repository.specifications;

import com.social.mc_post.dto.enums.TypePost;
import com.social.mc_post.structure.PostEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Date;

public class PostSpecification {

    public static Specification<PostEntity> getPostByTitle(String title){
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("title"), String.format("%%%s%%", title));
    }

    public static Specification<PostEntity> getPostType(TypePost type){
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("type"), type);
    }

    public static Specification<PostEntity> getPostByAuthor(String authorId){
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("authorId"), authorId);
    }

    public static Specification<PostEntity> getPostsByPublishDateBetween(Date to, Date from){
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.between(root.get("publishDate"), to, from);
    }

    public static Specification<PostEntity> getPostByPublishDate(LocalDateTime date){

        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.greaterThan(root.get("publishDate"), date);
    }

    public static Specification<PostEntity> getPostByPostText(String text){
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("postText"), text);
    }

}
