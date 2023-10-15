package com.study.oauth2.config;

import com.study.oauth2.handler.OAuth2LoginSuccessHandler;
import com.study.oauth2.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler, CustomOAuth2UserService customOAuth2UserService) {
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/login**", "/callback/", "/webjars/**", "/error**").permitAll()
                .antMatchers("/user").hasRole("USER")
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                .successHandler(oAuth2LoginSuccessHandler) // 성공 핸들러 등록;
                .userInfoEndpoint()
                .userService(customOAuth2UserService);
    }
}
