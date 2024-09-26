package com.social.mc_post.repository;

<<<<<<< HEAD
import com.social.mc_post.dto.enums.TypeComment;
import com.social.mc_post.structure.CommentEntity;
import com.social.mc_post.structure.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
=======
import com.social.mc_post.dto.CommentDto;
import com.social.mc_post.dto.enums.TypeComment;
import com.social.mc_post.model.Comment;
import com.social.mc_post.model.Post;
>>>>>>> 37f25c01f13c20b5d3ef933116c30d9e480c03c4
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

<<<<<<< HEAD
import java.util.List;
=======
import java.util.Collection;
import java.util.List;
import java.util.Optional;
>>>>>>> 37f25c01f13c20b5d3ef933116c30d9e480c03c4

@Repository
public interface CommentRepository extends JpaRepository<Comment, String>, JpaSpecificationExecutor<CommentDto> {

<<<<<<< HEAD
    CommentEntity findCommentEntityById(String id);
    @Modifying
    @Query(value = "UPDATE CommentEntity AS c SET c = :comment WHERE c.id = :id")
    void updateComment(@Param("comment") CommentEntity comment, @Param("id") String id);
    void deleteCommentEntityById(String id);
    List<CommentEntity> findByAuthorId(String authorId);
    Page<CommentEntity> findByCommentTypeAndPostId(TypeComment typeComment, String postId, Pageable page);
=======
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
>>>>>>> 37f25c01f13c20b5d3ef933116c30d9e480c03c4
}
