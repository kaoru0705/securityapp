package com.ch.securityapp.member.entity;

/* 데이터베이스의 테이블에 대한 정보를 최대한 자세히 표현해야 JPA 테이블에 제대로 반영 */

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@NoArgsConstructor
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "homepage_id", length = 20, nullable = false)
    private String homepageId;

    @Column(name = "password", length = 64, nullable = false)
    private String password;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    public Member(String homepageId, String password, String name){
        this.homepageId = homepageId;
        this.password = password;
        this.name = name;
    }
}