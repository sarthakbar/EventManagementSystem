package com.sb.security;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.sb.entity.User;
import com.sb.repository.UserRepository;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {

        OAuth2User oauthUser = super.loadUser(userRequest);

        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");

        userRepository.findByEmail(email).orElseGet(() -> {
            User user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setPassword("OAUTH2_USER");
            user.setPasswordChanged(true);
            return userRepository.save(user);
        });

        return oauthUser;
    }
}
