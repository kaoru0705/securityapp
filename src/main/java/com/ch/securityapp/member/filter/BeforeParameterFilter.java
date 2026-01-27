package com.ch.securityapp.member.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
    스프링 시큐리티의 요청 처리 흐름을 파악하기 위해 UsernamePasswordAuthenticationFilter 클래스를
    상속 받아 메서드를 오버라이드 한 후, 매개변수를 찍어볼 수도 있으나, 잘못하다간, 요청 필터 체인이 깨질 수 있기 때문에
    보다 가볍고도 필터 체인을 보호할 수 있는 방법을 사용해보기 위함
    UsernamePasswordAuthenticationFilter 앞에 나만의 필터를 두고서 여기서 파라미터를 낚아채어 출력...
 */
@Slf4j
public class BeforeParameterFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 다른 요청은 제외하고, 오직 로그인 요청에 대해서만 파라미터 확인해보기
        if(request.getRequestURI().equals("/api/auth/login") && "POST".equalsIgnoreCase(request.getMethod())) {   // http://localhost:9993/api/auth/login     /api/auth/login을 뽑아내기
            String homepageId = request.getParameter("homepageId");
            String password = request.getParameter("password");

            log.debug("클라이언트가 전송한 homepageId is {}, password is {}", homepageId, password);
        }
        // 요청의 흐름이 원래 가던 길을 갈 수 있도록, 흐름을 터줘야 한다. (BeforeParameterFilter에서 다시 Filter로 가도록)
        filterChain.doFilter(request, response);
    }
}
