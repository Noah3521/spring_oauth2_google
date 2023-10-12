package com.study.oauth2.service;

import com.study.oauth2.entity.SocialUser;
import com.study.oauth2.repository.SocialUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SocialUserService {

    private final SocialUserRepository socialUserRepository;

    public SocialUserService(SocialUserRepository socialUserRepository) {
        this.socialUserRepository = socialUserRepository;
    }

    @Transactional // 전부 성공 or 전부 실패
    public SocialUser save(SocialUser socialUser){
        return socialUserRepository.save(socialUser);
    }

}
