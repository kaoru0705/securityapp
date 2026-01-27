package com.ch.securityapp.member.dto;

import com.ch.securityapp.member.entity.Member;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/*
    PasswordEncode는 이 클래스에 저장된 비밀번호를 통해 검증을 하므로,
    UserDetailsService에서 회원정보를 가져오는데 성공한 직후, UserDetails에 회원정보를
    넣어둬야 한다.
 */

public class CustomUserDetails implements UserDetails {

    private final Long memberId;
    private final String homepageId;
    private final String password;

    public CustomUserDetails(Member member) {
        memberId = member.getMemberId();
        homepageId = member.getHomepageId();
        password = member.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 관리자 일반인 등등 ROLE_
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return homepageId;
    }
}
