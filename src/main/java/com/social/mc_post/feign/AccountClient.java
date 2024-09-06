package com.social.mc_post.feign;

import com.social.mc_post.dto.PageDto;
import com.social.mc_post.dto.account.AccountMeDTO;
import com.social.mc_post.dto.account.SearchDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "account", url = "http://79.174.80.200:8085/api/v1")
public interface AccountClient {

    @GetMapping("/account/search")
    Page<AccountMeDTO> getListAccounts(@RequestHeader("Authorization") String headerRequestByAuth,
                                              SearchDTO searchDTO, Pageable pageable);
}
