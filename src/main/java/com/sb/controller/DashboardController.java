package com.sb.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sb.entity.User;
import com.sb.repository.UserRepository;
import com.sb.security.CustomUserDetails;
import com.sb.service.EventService;
import com.sb.service.TicketService;

@Controller
public class DashboardController {

    private final EventService eventService;
    private final TicketService ticketService;
    private final UserRepository userRepository;

    public DashboardController(EventService eventService,
                               TicketService ticketService,
                               UserRepository userRepository) {
        this.eventService = eventService;
        this.ticketService = ticketService;
        this.userRepository = userRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {

        String email;

        Object principal = authentication.getPrincipal();

        // FORM LOGIN
        if (principal instanceof CustomUserDetails userDetails) {
            email = userDetails.getUsername();
        }
        // GOOGLE OAUTH LOGIN
        else if (principal instanceof OAuth2User oauthUser) {
            email = oauthUser.getAttribute("email");
        }
        else {
            throw new RuntimeException("Unsupported authentication type");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("createdEvents", eventService.getEventsCreatedBy(user));
        model.addAttribute("registeredTickets", ticketService.getTicketsByUser(user));
        model.addAttribute("userName", user.getName());

        return "dashboard";
    }
}
