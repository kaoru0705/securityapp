package com.ch.securityapp.member.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 스프링 요청 처리 흐름에서 (필터체인) 사용자 id를 조회하여 데이터베이스로부터 사용자 정보를 가져오는 역할을 수행하는
// 서비스를 커스텀해보기
@Service
@Slf4j
public class CustomDetailsService implements UserDetailsService {


    @Override
    public UserDetails loadUserByUsername(String homepageId) throws UsernameNotFoundException {
        log.debug("클라이언트가 전송한 파라미터를 서비스에서 출력 {}", homepageId);
        return null;
    }
}
