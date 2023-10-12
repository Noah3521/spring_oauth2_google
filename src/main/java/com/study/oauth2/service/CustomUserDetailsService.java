package com.study.oauth2.service;

import com.study.oauth2.entity.SocialUser;
import com.study.oauth2.repository.SocialUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final SocialUserRepository socialUserRepository;

    public CustomUserDetailsService(SocialUserRepository socialUserRepository) {
        this.socialUserRepository = socialUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SocialUser socialUser = socialUserRepository.findByEmail(username);
        if (socialUser == null) {
            throw new UsernameNotFoundException(username);
        }
        return new org.springframework.security.core.userdetails.User(socialUser.getEmail(), "", new ArrayList<>());
    }

    // other methods...
}
