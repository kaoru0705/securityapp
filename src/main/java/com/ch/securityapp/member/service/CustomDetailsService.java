package com.ch.securityapp.member.service;

import com.ch.securityapp.member.dto.CustomUserDetails;
import com.ch.securityapp.member.entity.Member;
import com.ch.securityapp.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 스프링 요청 처리 흐름에서 (필터체인) 사용자 id를 조회하여 데이터베이스로부터 사용자 정보를 가져오는 역할을 수행하는
// 서비스를 커스텀해보기
@Service
@Slf4j
@RequiredArgsConstructor
public class CustomDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String homepageId) throws UsernameNotFoundException {
        log.debug("클라이언트가 전송한 파라미터를 서비스에서 출력 {}", homepageId);

        // 일단 회원의 ID를 이용하여 회원정보를 가져온 후, UserDetails라는 일종의 DTO에 담아야 한다.
        // 그리고 나서,  PasswordEncoder가 UserDetails의 정보의 비밀번호를 비교함(반환을 받은 Provider 가 진행)
        // 인증 성공이 되면, Authentication에 성공 정보를 채워넣음, UsernamePasswordAuthenticaitonFilter에게 반환
        // UsernamePasswordAuthenticationFilter는 성공인 경우 SecurityContext에 성공 정보 저장 + 세션 저장
        // SuccessHandler 호출...  이 이후부터 개발자가 원하는 Controller에 요청
        Member member = memberRepository.findByHomepageId(homepageId).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원"));

        UserDetails userDetails = new CustomUserDetails(member);

        return userDetails;
    }
}
