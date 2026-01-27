package com.ch.securityapp.member.repository;

import com.ch.securityapp.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // JpaRepository 인터페이스는 필수적인 find ~~, findBy~ 등을 지원하기는 하지만,
    // 아주 필수적인 멤버 필드에 대해서만 한정적이므로 , 나머지 필드들에 대한 메서드를 개발자가 정의해야 함
    Optional<Member> findByHomepageId(String homepageId);
}