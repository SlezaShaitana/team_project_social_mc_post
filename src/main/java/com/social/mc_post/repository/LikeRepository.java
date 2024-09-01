package com.social.mc_post.repository;

import com.social.mc_post.dto.ReactionDto;
import com.social.mc_post.model.Comment;
import com.social.mc_post.model.Like;
import com.social.mc_post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, String> {

    @Query("select l from Like as l WHERE l.id =:id")
    Optional<Like> findById(String id);

    @Query("select l from Like as l WHERE l.comment =:comment")
    List<Like> findByComment(Comment comment);

    @Query("select l from Like as l WHERE l.post =:post")
    List<Like> findByPost(Post post);

    @Query("select count(*) from Like as l WHERE l.post =:post")
    Integer countByPost(Post post);
}
