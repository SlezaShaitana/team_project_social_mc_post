package com.social.mc_post.repository;

import com.social.mc_post.dto.enums.TypePost;
<<<<<<< HEAD
import com.social.mc_post.structure.PostEntity;
=======
import com.social.mc_post.model.Post;

>>>>>>> 37f25c01f13c20b5d3ef933116c30d9e480c03c4
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Repository
<<<<<<< HEAD
public interface PostRepository extends JpaRepository<PostEntity, String>, JpaSpecificationExecutor<PostEntity> {
    List<PostEntity> findAll();

    PostEntity findPostEntityById(String id);

    @Modifying
    @Query(value = "UPDATE PostEntity AS p SET p = :post WHERE p.id = :id")
    void updatePost(@Param("post") PostEntity post, @Param("id") String id);

    @Query("select p from PostEntity as p WHERE p.type =:type and p.authorId =:authorId")
    List<PostEntity> findByTypeAndAuthorId(TypePost type, String authorId);
=======
public interface PostRepository extends JpaRepository<Post, String>, JpaSpecificationExecutor<Post> {
    @Query("select p from Post as p WHERE p.type =:type and p.authorId =:authorId")
    List<Post> findByTypeAndAuthorId(TypePost type, String authorId);

    @Query("select p from Post as p WHERE p.authorId =:authorId")
    List<Post> findByAuthorId(String authorId);

    @Query("select p from Post as p WHERE p.type =:type")
    List<Post> findByType(TypePost type);
>>>>>>> 37f25c01f13c20b5d3ef933116c30d9e480c03c4

    @Query("select p from Post as p WHERE p.authorId IN (:authorIds) ")
    List<Post> findByAuthorIdList(List<String> authorIds);
}
