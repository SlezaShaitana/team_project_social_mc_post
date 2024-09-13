package com.social.mc_post.services.impl;

import com.social.mc_post.dto.RequestDto;
import com.social.mc_post.dto.ResponseStatisticDto;
import com.social.mc_post.exception.ResourceNotFoundException;
import com.social.mc_post.model.Comment;
import com.social.mc_post.model.Like;
import com.social.mc_post.model.Post;
import com.social.mc_post.repository.CommentRepository;
import com.social.mc_post.repository.LikeRepository;
import com.social.mc_post.repository.PostRepository;
import com.social.mc_post.security.DecodedToken;
import com.social.mc_post.services.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    @Override
    public ResponseStatisticDto getStatisticPost(RequestDto requestDto, String headerRequestByAuth) {
        try {
            List<String> ids = List.of(getAuthorId(headerRequestByAuth));
            LocalDateTime from = requestDto.getFirstMonth() == null ? getDate("1970-01-01T03:00:00.000Z")
                    : getDate(requestDto.getFirstMonth());
            LocalDateTime to = requestDto.getLastMonth() == null ? LocalDateTime.now()
                    : getDate(requestDto.getLastMonth());
            List<Post> posts = postRepository.findByAuthorIdList(ids).stream()
                    .filter(post -> post.getTime().isAfter(from) && post.getTime().isBefore(to))
                    .toList();
            ResponseStatisticDto statistic = new ResponseStatisticDto();
            statistic.setTime(LocalDateTime.now());
            statistic.setPeriodTo(to);
            statistic.setPeriodFrom(from);
            statistic.setCountResult(posts.size());
            return statistic;
        }catch (Exception e){
            throw new ResourceNotFoundException("Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseStatisticDto getStatisticComment(RequestDto requestDto, String headerRequestByAuth) {
        try {
            String id = getAuthorId(headerRequestByAuth);
            LocalDateTime from = requestDto.getFirstMonth() == null ? getDate("1970-01-01T03:00:00.000Z")
                    : getDate(requestDto.getFirstMonth());
            LocalDateTime to = requestDto.getLastMonth() == null ? LocalDateTime.now()
                    : getDate(requestDto.getLastMonth());
            List<Comment> comments = commentRepository.findByAuthorId(id).stream()
                    .filter(comment -> comment.getTime().isAfter(from) && comment.getTime().isBefore(to))
                    .toList();
            ResponseStatisticDto statistic = new ResponseStatisticDto();
            statistic.setTime(LocalDateTime.now());
            statistic.setPeriodTo(to);
            statistic.setPeriodFrom(from);
            statistic.setCountResult(comments.size());
            return statistic;
        }catch (Exception e){
            throw new ResourceNotFoundException("Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseStatisticDto getStatisticLike(RequestDto requestDto, String headerRequestByAuth) {
        try {
            String id = getAuthorId(headerRequestByAuth);
            LocalDateTime from = requestDto.getFirstMonth() == null ? getDate("1970-01-01T03:00:00.000Z")
                    : getDate(requestDto.getFirstMonth());
            LocalDateTime to = requestDto.getLastMonth() == null ? LocalDateTime.now()
                    : getDate(requestDto.getLastMonth());
            List<Like> likes = likeRepository.findByAuthorId(id).stream()
                    .filter(like -> like.getTime().isAfter(from) && like.getTime().isBefore(to))
                    .toList();
            ResponseStatisticDto statistic = new ResponseStatisticDto();
            statistic.setTime(LocalDateTime.now());
            statistic.setPeriodTo(to);
            statistic.setPeriodFrom(from);
            statistic.setCountResult(likes.size());
            return statistic;
        }catch (Exception e){
            throw new ResourceNotFoundException("Error: " + e.getMessage());
        }
    }

    private String getAuthorId(String headerRequestByAuth) throws UnsupportedEncodingException {
        String stringToken = headerRequestByAuth.substring(7);
        DecodedToken decodedToken = DecodedToken.getDecoded(stringToken);
        return decodedToken.getId();
    }

    private LocalDateTime getDate(String stringDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");
        return LocalDateTime.parse(stringDate, formatter);
    }
}
