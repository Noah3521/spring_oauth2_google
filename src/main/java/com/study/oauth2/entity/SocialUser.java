package com.study.oauth2.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "social_users")
@Getter
@Setter
public class SocialUser {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    // 소셜 로그인 제공자 이름 (Google, Naver, Kakao 등)
    @Column(name="provider")
    private String provider;

    // 유저고유번호(아이디)
    @Column(name="username")
    private String username;

    // 해당 제공자에서의 고유 식별자
    @Column(name="provider_id")
    private String providerId;

    // 이메일 주소
    @Column(nullable=false, unique=true)
    private String email;

    // 이름
    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy="role", cascade=CascadeType.ALL)
    private Set<UserRole> userRoles;


}