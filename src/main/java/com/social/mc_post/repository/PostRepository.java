package com.social.mc_post.repository;

import com.social.mc_post.structure.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface PostRepository extends JpaRepository<PostEntity, String>, JpaSpecificationExecutor<PostEntity> {
    List<PostEntity> findAll();
    PostEntity findPostEntityById(String id);

    @Query(value = "SELECT p FROM PostEntity AS p GROUP BY p.publishDate DESC LIMIT 5", nativeQuery = true)
    Page<PostEntity> getPosts(Pageable page);

    @Modifying
    @Query(value = "UPDATE PostEntity AS p SET p = :post WHERE p.id = :id")
    void updatePost(@Param("post") PostEntity post, @Param("id") String id);



}
