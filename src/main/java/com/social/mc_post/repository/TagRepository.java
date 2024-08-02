package com.social.mc_post.repository;


import com.social.mc_post.structure.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, String> {

    @Query(value = "SELECT * FROM tags", nativeQuery = true)
    List<TagEntity> getAllTags();
}
