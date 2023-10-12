package com.study.oauth2.handler;

import com.study.oauth2.entity.SocialUser;
import com.study.oauth2.repository.SocialUserRepository;
import com.study.oauth2.service.SocialUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final SocialUserService userService;
    private final SocialUserRepository socialUserRepository;

    public OAuth2LoginSuccessHandler(SocialUserService userService, SocialUserRepository userRepository){
        this.userService = userService;
        this.socialUserRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            String provider = "google"; // 현재는 Google만 고려. 추후 다른 제공자 추가 필요.
            String providerId = oauth2User.getAttribute("sub"); // Google ID 가져오기

            // 반환되는 데이터 출력
//            Map<String, Object> attributes = oauth2User.getAttributes();
//            for (Map.Entry<String, Object> entry : attributes.entrySet()) {
//                System.out.println(entry.getKey() + ": " + entry.getValue());
//            }

            SocialUser socialUser = socialUserRepository.findByProviderAndProviderId(provider, providerId).orElseGet(() -> {
                SocialUser newSocialUser = new SocialUser();
                newSocialUser.setUsername(provider+"_"+providerId); // ex) google_12345
                newSocialUser.setProvider(provider); // ex) google
                newSocialUser.setProviderId(providerId); // 고유 번호
                newSocialUser.setName(oauth2User.getAttribute("name")); // 이름 설정하기
                newSocialUser.setEmail(oauth2User.getAttribute("email"));

                return socialUserRepository.save(newSocialUser);
            });

            System.out.println("Logged in with user: " + socialUser.getUsername());
        }
    }
}
