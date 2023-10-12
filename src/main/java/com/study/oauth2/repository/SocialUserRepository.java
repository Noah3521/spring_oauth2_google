package com.study.oauth2.repository;

import com.study.oauth2.entity.SocialUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialUserRepository extends JpaRepository<SocialUser, Long> {
    SocialUser findByEmail(String username);

    // 변경: provider와 providerId로 사용자 조회하기
    Optional<SocialUser> findByProviderAndProviderId(String provider, String providerId);

}
