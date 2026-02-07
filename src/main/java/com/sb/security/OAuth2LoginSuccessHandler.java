package com.sb.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.sb.entity.User;
import com.sb.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    public OAuth2LoginSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        String email = oauthUser.getAttribute("email");
        String name  = oauthUser.getAttribute("name");

        // create user if not exists
        userRepository.findByEmail(email).orElseGet(() -> {
            User user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setPassword("OAUTH2_USER"); // dummy
            user.setPasswordChanged(true);
            return userRepository.save(user);
        });

        response.sendRedirect("/dashboard");
    }
}
