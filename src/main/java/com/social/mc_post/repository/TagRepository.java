package com.social.mc_post.repository;


import com.social.mc_post.model.Post;
import com.social.mc_post.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {
    List<Tag> findByPost(Post post);
}
