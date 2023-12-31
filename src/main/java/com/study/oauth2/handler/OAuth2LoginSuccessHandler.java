package com.study.oauth2.handler;

import com.study.oauth2.entity.Role;
import com.study.oauth2.entity.SocialUser;
import com.study.oauth2.entity.UserRole;
import com.study.oauth2.repository.RoleRepository;
import com.study.oauth2.repository.SocialUserRepository;
import com.study.oauth2.repository.UserRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@Component
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final SocialUserRepository socialUserRepository;

    private final RoleRepository roleRepository;

    private final UserRoleRepository userRoleRepository;

    public OAuth2LoginSuccessHandler(SocialUserRepository userRepository, RoleRepository roleRepository, UserRoleRepository userRoleRepository){
        this.socialUserRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        if (authentication.getPrincipal() instanceof OAuth2User) {

            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

            String provider;

            HttpSession session = request.getSession(false);
            if (session != null) {
                provider = (String) session.getAttribute("provider");
                session.removeAttribute("provider");
            } else {
                throw new IllegalStateException("provider를 가져올 수 없습니다.");
            }

            String providerId;
            if(provider.equals("google")){
                providerId = oauth2User.getAttribute("sub"); // Google ID 가져오기
            } else if (provider.equals("naver")){
                providerId = oauth2User.getAttribute("id");  // Naver Id
            }
            else {
                throw new IllegalStateException("알 수 없는 서비스입니다.");
            }
            log.info("서비스: "+provider);


//             반환되는 데이터 출력
            Map<String, Object> attributes = oauth2User.getAttributes();
            for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

            SocialUser socialUser = socialUserRepository.findByProviderAndProviderId(provider, providerId).orElseGet(() -> {

                SocialUser newSocialUser = new SocialUser();

                newSocialUser.setUsername(provider+"_"+providerId); // ex) google_12345
                newSocialUser.setProvider(provider); // ex) google
                newSocialUser.setProviderId(providerId); // 고유 번호
                newSocialUser.setName(oauth2User.getAttribute("name")); // 이름 설정하기
                newSocialUser.setEmail(oauth2User.getAttribute("email"));

                Role userRole = roleRepository.findByName("ROLE_USER")  // user 권한 찾기
//                        .orElseThrow(() -> new RuntimeException("Error: Role is not found.")); // 없으면 예외
                        .orElseGet(() -> {                                   // 없으면 새롭게 생성
                            Role newUserRole = new Role();
                            newUserRole.setName("ROLE_USER");
                            return roleRepository.save(newUserRole);
                        });

                UserRole userRoleEntity = new UserRole();   // 새로운 UserRole 엔티티 생성
                userRoleEntity.setRole(userRole);           // Role 설정
                userRoleEntity.setSocialUser(newSocialUser);

                socialUserRepository.save(newSocialUser);

                userRoleEntity.setSocialUser(newSocialUser);

                userRoleRepository.save(userRoleEntity);

                return newSocialUser;
            });
            response.sendRedirect("/");
            log.info("Logged in with user: " + socialUser.getName());
        }
    }

}
