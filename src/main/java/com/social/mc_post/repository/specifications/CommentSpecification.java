package com.social.mc_post.repository.specifications;

import com.social.mc_post.dto.enums.TypeComment;
import com.social.mc_post.structure.CommentEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CommentSpecification {

    public Specification<CommentEntity> findCommentsByCommentType(TypeComment type){
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("commentType"), type);
    }

    public Specification<CommentEntity> findCommentsByAuthorId(String authorId){
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("authorId"), authorId);
    }

    public Specification<CommentEntity> findCommentsByPostId(String postId){
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("postId"), postId);
    }

    public Specification<CommentEntity> findCommentsByParentId(String parentId){
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("parentId"), parentId);
    }

    public Specification<CommentEntity> findCommentsById(String Id){
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("Id"), Id);
    }
}
