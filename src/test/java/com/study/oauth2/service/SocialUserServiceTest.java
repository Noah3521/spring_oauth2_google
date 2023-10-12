package com.study.oauth2.service;

import com.study.oauth2.entity.SocialUser;
import com.study.oauth2.repository.SocialUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SocialUserServiceTest {

    @Autowired
    private SocialUserService sociaUserService;

    @Autowired
    private SocialUserRepository socialUserRepository;

    @Test
    public void userInsertTest() {
        SocialUser socialUser = new SocialUser();
        socialUser.setUsername("testuser");
//        socialUser.setPassword("testpassword");
        socialUser.setEmail("test@test.com");
        socialUser.setName("test");

        SocialUser savedUser = sociaUserService.save(socialUser);

        SocialUser foundUser = socialUserRepository.findById(savedUser.getId()).orElse(null);

        assertNotNull(foundUser);
        assertEquals(savedUser.getUsername(), foundUser.getUsername());
    }

}
