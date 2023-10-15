package com.study.oauth2.controller;

import com.study.oauth2.service.SocialUserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/socialuser")
public class SocialUserController {

    private final SocialUserService socialUserService;

    public SocialUserController(SocialUserService userService){
        this.socialUserService = userService;
    }

    @GetMapping("/user")
    public OAuth2User user(@AuthenticationPrincipal OAuth2User principal) {
        return principal;
    }

}