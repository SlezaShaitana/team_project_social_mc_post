package com.social.mc_post.repository;

import com.social.mc_post.dto.enums.TypePost;
import com.social.mc_post.model.Post;

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
public interface PostRepository extends JpaRepository<Post, String>, JpaSpecificationExecutor<Post> {
    @Query("select p from Post as p WHERE p.type =:type and p.authorId =:authorId")
    List<Post> findByTypeAndAuthorId(TypePost type, String authorId);

    @Query("select p from Post as p WHERE p.authorId =:authorId")
    List<Post> findByAuthorId(String authorId);

    @Query("select p from Post as p WHERE p.type =:type")
    List<Post> findByType(TypePost type);

    @Query("select p from Post as p WHERE p.authorId IN (:authorIds) ")
    List<Post> findByAuthorIdList(List<String> authorIds);
}
