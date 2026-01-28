package com.ch.securityapp.member.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class MemberController {
    @GetMapping("/")
    public String getMain() {
        return "this is my spring security application";
    }

    /* 로그인된 사용자가 자신의 정보를 요청할 때를 처리하는 메서드
        반환 값 {"profile": "sdfdsfds"}
    */
    @GetMapping("/api/me")
    public Map<String, Object> getMyInfo(Authentication authentication) {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User user = token.getPrincipal();

        log.debug("provider is {}", token.getAuthorizedClientRegistrationId());    // provider 가져오기
        log.debug("name is " + user.getAttribute("name"));
        log.debug("email is " + user.getAttribute("email"));

        Map<String, Object> result = new HashMap<>();
        result.put("provider", token.getAuthorizedClientRegistrationId());
        result.put("name", user.getAttribute("name"));
        result.put("email", user.getAttribute("email"));

        return result;
    }
}