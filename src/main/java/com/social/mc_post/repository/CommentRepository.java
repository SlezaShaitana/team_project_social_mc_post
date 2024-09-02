package com.social.mc_post.repository;

import com.social.mc_post.dto.enums.TypeComment;
import com.social.mc_post.structure.CommentEntity;
import com.social.mc_post.structure.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, String>, JpaSpecificationExecutor<CommentEntity> {

    CommentEntity findCommentEntityById(String id);
    @Modifying
    @Query(value = "UPDATE CommentEntity AS c SET c = :comment WHERE c.id = :id")
    void updateComment(@Param("comment") CommentEntity comment, @Param("id") String id);
    void deleteCommentEntityById(String id);
    List<CommentEntity> findByAuthorId(String authorId);
    Page<CommentEntity> findByCommentTypeAndPostId(TypeComment typeComment, String postId, Pageable page);
}
