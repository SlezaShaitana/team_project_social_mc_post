package com.social.mc_post.repository;

import com.social.mc_post.structure.CommentSearchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentSearchRepository extends JpaRepository<CommentSearchEntity, String> {
}
