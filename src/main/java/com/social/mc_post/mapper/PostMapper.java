package com.social.mc_post.mapper;

import com.social.mc_post.dto.PostDto;
import com.social.mc_post.dto.ReactionDto;
import com.social.mc_post.dto.TagDto;
import com.social.mc_post.dto.enums.TypeLike;
import com.social.mc_post.model.Like;
import com.social.mc_post.model.Post;
import com.social.mc_post.repository.CommentRepository;
import com.social.mc_post.repository.LikeRepository;
import com.social.mc_post.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PostMapper {

    private final CommentRepository commentRepository;
    private final TagRepository tagRepository;
    private final LikeRepository likeRepository;
    private final TagMapper tagMapper;

    public PostDto mapEntityToDto(Post post){
        return PostDto.builder()
                .id(post.getId())
                .isDeleted(post.getIsDeleted())
                .time(post.getTime())
                .timeChanged(post.getTimeChanged())
                .authorId(post.getAuthorId())
                .title(post.getTitle())
                .type(post.getType())
                .postText(post.getPostText())
                .isBlocked(post.getIsBlocked())
                .tags(getTagDtoList(post))
                .reactions(getReactionDtoList(post))
                .myReaction(post.getMyReaction())
                .likeAmount(likeRepository.countByPostAndType(post, TypeLike.POST))
                .commentsCount(commentRepository.countByPost(post))
                .myLike(post.getMyLike())
                .imagePath(post.getImagePath())
                .publishDate(post.getPublishDate())
                .build();
    }

    private List<ReactionDto> getReactionDtoList(Post post){
        List<Like> likes = likeRepository.findByPost(post);
        Map<String, Integer> reactionMap = new HashMap<>();
        for (Like like : likes){
            if (like.getReaction() != null){
                reactionMap.put(like.getReaction(), likeRepository.countByPostAndReaction(post
                        , like.getReaction()));
            }
        }

        List<ReactionDto> reactions = new ArrayList<>();
        for (Map.Entry<String,Integer> entry : reactionMap.entrySet()){
            reactions.add(new ReactionDto(entry.getKey(),entry.getValue()));
        }
        return reactions;
    }

    private List<TagDto> getTagDtoList(Post post){
        return tagRepository.findByPost(post).stream()
                .map(tagMapper::mapEntityToDto)
                .toList();
    }
}
