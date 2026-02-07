package com.sb.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.sb.entity.User;
import com.sb.repository.UserRepository;

import java.io.IOException;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    public CustomLoginSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        String email = null;

        Object principal = authentication.getPrincipal();

        // FORM LOGIN
        if (principal instanceof UserDetails userDetails) {
            email = userDetails.getUsername();
        }

        // GOOGLE OAUTH LOGIN
        else if (principal instanceof OAuth2User oauthUser) {
            email = oauthUser.getAttribute("email");
        }

        if (email == null) {
            response.sendRedirect("/login");
            return;
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found after login"));

        
        if (Boolean.FALSE.equals(user.getPasswordChanged())) {
            response.sendRedirect("/change-password");
        } else {
            response.sendRedirect("/dashboard");
        }
    }
}
