package com.study.oauth2.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        // 여기서 provider 정보를 세션에 저장합니다.
        String provider = userRequest.getClientRegistration().getRegistrationId();
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
        session.setAttribute("provider", provider);
        if ("naver".equalsIgnoreCase(userRequest.getClientRegistration().getRegistrationId())) {
            return processNaverOauth(user);
        }

        return user;
    }

    private OAuth2User processNaverOauth(OAuth2User oAuth2User) {
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());

        if (attributes.get("response") instanceof Map) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            attributes.putAll(response);
            attributes.remove("response");
        }

        return new DefaultOAuth2User(
                oAuth2User.getAuthorities(),
                attributes,
                "id" // 네이버의 경우 id 필드가 고유 식별자 역할을 합니다.
        );
    }
}
