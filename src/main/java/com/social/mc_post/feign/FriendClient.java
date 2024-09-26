package com.social.mc_post.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
<<<<<<< HEAD

import java.util.List;
=======
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.UUID;
>>>>>>> 37f25c01f13c20b5d3ef933116c30d9e480c03c4

@FeignClient(value = "friend", url = "http://79.174.80.200:8090/api/v1/friends")
public interface FriendClient {

    @GetMapping("/friendId/{id}")
<<<<<<< HEAD
    List<String> getFriendsIdListByUserId(@PathVariable("id") String id);
}

=======
    List<UUID> getFriendsIdListByUserId(@RequestHeader("Authorization") String headerRequestByAuth, @PathVariable("id") String id);
}
>>>>>>> 37f25c01f13c20b5d3ef933116c30d9e480c03c4
