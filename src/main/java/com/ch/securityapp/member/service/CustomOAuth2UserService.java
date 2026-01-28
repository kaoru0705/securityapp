package com.ch.securityapp.member.service;

/*
    프로바이더마다 응답 정보를 구성하는 사용자 정보 키값들이 다르기 때문에,
    컨트롤러에서 조건을 따져보면서 응답 정보를 처리해야 하는 상황...
    but 컨트롤러가 지저분해지면 안되므로, 응답 정보를 구성해주는 전용 서비스를 정의
 */

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();    // google, naver, kakao 등 provider

        Map<String, Object> normalized = new HashMap<>();   // 서로 다른 프로바이더들간의 차이를 극복하기 위한, 통일 시키기 위한 맵
        Map<String, Object> attrs = oAuth2User.getAttributes();     // 로그인한 결과인 응답 정보(구글, 네이버, 카카오)

        if(provider.equals("google")) {
            normalized.put("openId", attrs.get("sub"));     // 구글의 사용자 구분 식별 아이디 openid
            normalized.put("name", attrs.get("name"));
            normalized.put("email", attrs.get("email"));
        }

        if(provider.equals("naver")) {
            // attrs: {resultcode..., message..., response:{id, email, name}}
            Map<String, Object> response = (Map<String, Object>) attrs.get("response");
            if(response != null) {
                normalized.put("openId", response.get("id"));     // 네이버의 사용자 구분 식별 아이디 openid
                normalized.put("name", response.get("name"));
                normalized.put("email", response.get("email"));
            }
        }

        if(provider.equals("kakao")) {
            // {id: ......., kakao_account: {email...}, properties:{nickname...}}
            normalized.put("openId", attrs.get("id"));

            Map<String, Object> properties = (Map<String, Object>) attrs.get("properties");
            normalized.put("name", properties.get("nickname"));

            Map<String, Object> kakaoAccount = (Map<String, Object>) attrs.get("kakao_account");
            if(kakaoAccount != null) normalized.put("email", kakaoAccount.get("email"));
        }

        // 권한
        Collection< GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        return new DefaultOAuth2User(authorities, normalized, "openId");
    }
}
