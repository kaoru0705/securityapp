package com.ch.securityapp.member.config;

/* 스프링 시큐리티의 빈설정, 필터 처리 흐름을 제어할 수 있는 가장 중요한 클래스 */

import com.ch.securityapp.member.filter.BeforeParameterFilter;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    /*-------------------------------------------------------------------------------
     CORS(Cross Origin Resource Sharing) 를 허용하기 위한 설정
     - 브라우저는 기본적으로 교차된 출처 리소스 공유를 금지하기 때문에, 서버가 허락한 출처에 한해서만
     예외적으로 서버에 요청을 허용한다.
     예로 들어 localhost:7777에서 localhost:9992로 요청을 보낸다면?
     Access to fetch at 'http://localhost:9992/api/auth/login' from origin 'http://localhost:7777' has been blocked
     by CORS policy: Response to preflight request doesn't pass access control check: No 'Access-Control-Allow-Origin' header is present on the requested resource.
     -------------------------------------------------------------------------------*/
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // CORS 정책을 담는 설정(허용할 출처/메서드(GET, POST...)/헤더 등) 객체
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173")); // 금지사항!!! * 패턴금지 정확히 적어야 됨
        // Cross Origin 때문에 OPTIONS는 크롬브라우저가 로그인 패스 요청을 날리기 앞서서 preflight(시험 비행)로 허용할지 실험
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));     // header는 * 패턴 가능 혹시 보안을 더 강화할 일이 있다면, 헤더를 지정하는 게 좋다.
        // 로그인 성공 시 세션 쿠키에서 웹브라우저가 요청을 할 때 톰캣이 session Id를 줘야 함 true로 설정
        config.setAllowCredentials(true);   // 만일 true로 주지 않으면, 브라우저가 쿠키를 보내지 않거나 응답을 막음
        config.setMaxAge(3600L);    // 3600 초 동안 동일 조건이라면 preflight를 매번 하지 않음

        // 허용할 URI 패턴 우리의 경우 /api/**
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**",  config);


        return source;
    }

    // 아래의 설정이 실제적으로 개발자가 직접 로그인과 관련된 설정을 담당
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors(cors -> {});  // 위에 등록한 빈 설정을 사용하여 처리..
        // 사이트 위조 해킹에 대한 방지 방법
        httpSecurity.csrf(csrf -> csrf.disable());  // 사이트 변조 공격 방지 비활성화

        // 테스트 목적   filter 전에 BeforeParameterFilter 실행하기
        httpSecurity.addFilterBefore(new BeforeParameterFilter(), UsernamePasswordAuthenticationFilter.class);

        httpSecurity.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()    // 접근 허용할 패턴 로그인페이지에서 인증 페이지로 가게 할 필요는 없으니
                .anyRequest().authenticated()   // 나머지 요청들에 대해선 인증 처리
        );

        // 스프링이 기본으로 제공해주는 로그인폼을 쓰지는 않지만, 필터 체인을 그대로 타고 들어갈 수 있도록 설정..

        httpSecurity.formLogin(form -> form
                .loginProcessingUrl("/api/auth/login") // 스프링이 지원하는 디폴트 로그인 요청 URL을 사용하지 않고, 개발자가 원하는 것으로 URL 바꿈
                .usernameParameter("homepageId")    // 스프링에게 로그인 파라미터 중 ID 변수명을 알려주고(기본 로그인 폼을 사용하지 않으니 알려줘야 함)
                .passwordParameter("password")
        );

        return httpSecurity.build();
    }
}
