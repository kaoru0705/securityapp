package com.ch.securityapp.member.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/*
    UsernamePasswordAuthenticationFilter가 성공한 Authentication 토큰을 받은 경우
    SecurityContext에 성공 결과를 먼저 저장하고나서, 성공에 대한 응답을 처리하는
    핸들러인 AuthenticationSuccessHandler를 호출
 */
@Slf4j
@Component
public class JsonSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.debug("인증 성공");

        response.setContentType("application/json;charset=UTF-8");  // header값 설정
        response.getWriter().write("{\"ok\": true, \"name\":\"" + authentication.getName() + "\" }");
    }
}
