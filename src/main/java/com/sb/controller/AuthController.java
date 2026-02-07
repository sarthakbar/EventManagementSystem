package com.sb.controller;

import java.security.Principal;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sb.entity.User;
import com.sb.repository.UserRepository;
import com.sb.service.EmailService;
import com.sb.util.PasswordGenerator;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    


    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService=emailService;
    }

    // Show login page
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    // Show registration page
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }
    
    @GetMapping("/change-password")
    public String showChangePassword() {
        return "auth/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Principal principal) {

        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("Passwords do not match");
        }

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordChanged(true);

        userRepository.save(user);

        return "redirect:/dashboard";
    }


    // Handle registration
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {

    	 // Duplicate email check
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            redirectAttributes.addFlashAttribute(
                "errorMessage",
                "❌ Email already registered. Please login."
            );
            return "redirect:/register";
        }
    	
        String tempPassword = PasswordGenerator.generateTempPassword();
        user.setPassword(passwordEncoder.encode(tempPassword));
        user.setPasswordChanged(false);
        userRepository.save(user);
        emailService.sendPasswordEmail(user.getEmail(), tempPassword);

        return "redirect:/login?registered";
    }

}
