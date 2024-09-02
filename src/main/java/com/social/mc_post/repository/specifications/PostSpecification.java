package com.social.mc_post.repository.specifications;

import com.social.mc_post.dto.PostSearchDto;
import com.social.mc_post.dto.enums.TypePost;
import com.social.mc_post.structure.PostEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
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

    public static Specification<PostEntity> findWithFilter(PostSearchDto filter) {
        return Specification.where(byDateToFrom(filter.getDateTo(), filter.getDateFrom()))
                .and(byIsDeleted(filter.getIsDeleted()));
    }

    private static Specification<PostEntity> byIsDeleted(Boolean isDeleted){
        return (root, query, criteriaBuilder) -> {
            if (isDeleted == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("isDeleted"), isDeleted);
        };
    }


    private static Specification<PostEntity> byDateToFrom(Date to, Date from){
        return (Root<PostEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            LocalDate now = LocalDate.now();
            TemporalAmount amountTo = (TemporalAmount) to;
            TemporalAmount amountFrom = (TemporalAmount) from;
            if (to == null && from == null) {
                return criteriaBuilder.conjunction();
            } else if (to == null) {
                LocalDate timeFrom = now.minus(amountFrom);
                return criteriaBuilder.lessThanOrEqualTo(root.get("time"), timeFrom);
            } else if (from == null) {
                LocalDate timeTo = now.minus(amountTo);
                return criteriaBuilder.greaterThanOrEqualTo(root.get("time"), timeTo);
            }

            LocalDate timeFrom = now.minus(amountFrom);
            LocalDate timeTo = now.minus(amountTo);
            return criteriaBuilder.and(
                    criteriaBuilder.lessThanOrEqualTo(root.get("time"), timeFrom),
                    criteriaBuilder.greaterThanOrEqualTo(root.get("time"), timeTo)
            );
        };
    }


}
