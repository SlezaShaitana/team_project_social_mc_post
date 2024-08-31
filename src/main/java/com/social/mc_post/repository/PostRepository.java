package com.social.mc_post.repository;

import com.social.mc_post.dto.enums.TypePost;
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
public interface PostRepository extends JpaRepository<PostEntity, String>, JpaSpecificationExecutor<PostEntity> {
    Page<PostEntity> findAllByAuthorId(String authorId, Pageable pageable);

    PostEntity findPostEntityById(String id);

    @Modifying
    @Query(value = "UPDATE PostEntity AS p SET p = :post WHERE p.id = :id")
    void updatePost(@Param("post") PostEntity post, @Param("id") String id);

    @Query("select p from PostEntity as p WHERE p.type =:type and p.authorId =:authorId")
    List<PostEntity> findByTypeAndAuthorId(TypePost type, String authorId);

}
