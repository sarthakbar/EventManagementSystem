package com.sb.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import com.sb.entity.User;
import com.sb.repository.UserRepository;

@Component
public class LoggedInUserUtil {

    private final UserRepository userRepository;

    public LoggedInUserUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(Authentication authentication) {

        // GOOGLE OAUTH LOGIN
        if (authentication.getPrincipal() instanceof OAuth2User oauthUser) {

            String email = oauthUser.getAttribute("email");
            String name  = oauthUser.getAttribute("name");

            return userRepository.findByEmail(email)
                    .orElseGet(() -> {
                        User user = new User();
                        user.setEmail(email);
                        user.setName(name);
                        user.setPassword("OAUTH2_USER");
                        user.setPasswordChanged(true);
                        return userRepository.save(user);
                    });
        }

        // NORMAL LOGIN
        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Authenticated user not found"));
    }
}
