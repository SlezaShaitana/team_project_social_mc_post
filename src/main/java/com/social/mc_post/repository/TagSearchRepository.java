package com.social.mc_post.repository;

import com.social.mc_post.structure.TagSearchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagSearchRepository extends JpaRepository<TagSearchEntity, String> {
}
