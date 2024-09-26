package com.social.mc_post.repository;

import com.social.mc_post.dto.ReactionDto;
import com.social.mc_post.dto.enums.TypeLike;
import com.social.mc_post.model.Comment;
import com.social.mc_post.model.Like;
import com.social.mc_post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, String> {

    @Query("select l from Like as l WHERE l.comment =:comment")
    List<Like> findByComment(Comment comment);

    @Query("select l from Like as l WHERE l.post =:post")
    List<Like> findByPost(Post post);

    @Query("select count(*) from Like as l WHERE l.post =:post")
    Integer countByPost(Post post);

    @Query("select count(*) from Like as l WHERE l.comment =:comment")
    Integer countByComment(Comment comment);

    @Query("select l from Like as l WHERE l.post =:post and l.authorId =:authorId and l.type =:type")
    Like foundByPostAndAuthorId(Post post, String authorId, TypeLike type);

    @Query("select count(*) from Like as l WHERE l.post =:post and l.reaction =:reaction")
    Integer countByPostAndReaction(Post post, String reaction);

    @Query("select count(*) from Like as l WHERE l.post =:post and l.type =:type")
    Integer countByPostAndType(Post post, TypeLike type);

    @Query("select l from Like as l WHERE l.comment =:comment and l.authorId =:authorId and l.type =:type")
    Like findByCommentAndAuthorId(Comment comment, String authorId, TypeLike type);

    @Query("select l from Like as l WHERE l.authorId =:authorId")
    List<Like> findByAuthorId(String authorId);
}
