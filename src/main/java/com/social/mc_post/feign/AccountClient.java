package com.social.mc_post.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(value = "account", url = "http://79.174.80.200:9095/api/v1/account")
public interface AccountClient {

    @GetMapping("/search_by_fullName")
    List<UUID> getListIdsAccounts(@RequestHeader("Authorization") String headerRequestByAuth,
                                       @RequestParam String firstName,
                                       @RequestParam String lastName);
}
