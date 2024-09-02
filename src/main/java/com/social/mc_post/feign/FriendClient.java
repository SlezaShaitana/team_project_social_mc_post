package com.social.mc_post.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "friend", url = "http://79.174.80.200:8090/api/v1/friends")
public interface FriendClient {

    @GetMapping("/friendId/{id}")
    List<String> getFriendsIdListByUserId(@PathVariable("id") String id);
}

