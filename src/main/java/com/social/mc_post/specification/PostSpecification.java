package com.social.mc_post.specification;


import com.social.mc_post.dto.PostSearchDto;
import com.social.mc_post.dto.enums.TypePost;
import com.social.mc_post.model.Post;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.temporal.TemporalAmount;
import java.util.Date;

public interface PostSpecification {

    static Specification<Post> findWithFilter(PostSearchDto filter) {
        return Specification.where(byDateToFrom(filter.getDateTo(), filter.getDateFrom()))
                .and(byIsDeleted(filter.getIsDeleted()));
    }

    static Specification<Post> byIsDeleted(Boolean isDeleted){
        return (root, query, criteriaBuilder) -> {
            if (isDeleted == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("isDeleted"), isDeleted);
        };
    }


    static Specification<Post> byDateToFrom(Date to, Date from){
        return (Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
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