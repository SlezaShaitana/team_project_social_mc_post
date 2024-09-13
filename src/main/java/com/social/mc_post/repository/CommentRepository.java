package com.social.mc_post.repository;

import com.social.mc_post.dto.CommentDto;
import com.social.mc_post.dto.enums.TypeComment;
import com.social.mc_post.model.Comment;
import com.social.mc_post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String>, JpaSpecificationExecutor<CommentDto> {

    @Query("select c from Comment as c WHERE c.post =:post")
    List<Comment> findByPost(Post post);

    @Query("select count(*) from Comment as c WHERE c.post =:post")
    Integer countByPost(Post post);

    @Query("select count(*) from Comment as c WHERE c.parentCommentId =:parentCommentId")
    Integer countByParentCommentId(String parentCommentId);

    @Query("select c from Comment as c WHERE c.parentCommentId =:parentId")
    List<Comment> findByParentCommentId(String parentId);

    @Query("select c from Comment as c WHERE c.authorId =:authorId")
    List<Comment> findByAuthorId(String authorId);
}
