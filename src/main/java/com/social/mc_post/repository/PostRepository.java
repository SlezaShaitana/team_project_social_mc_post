package com.social.mc_post.repository;

import com.social.mc_post.structure.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, String> {
    List<PostEntity> findAll();
    PostEntity findPostEntityById(String id);
    Page<PostEntity> findPageOfPostByPublishDateBetweenOrderByPublishDate(Date to, Date from, Pageable page);

    @Modifying
    @Query(value = "UPDATE PostEntity AS p SET p = :post WHERE p.id = :id")
    void updatePost(@Param("post") PostEntity post, @Param("id") String id);


}
