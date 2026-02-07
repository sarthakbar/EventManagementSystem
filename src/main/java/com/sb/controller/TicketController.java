package com.sb.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sb.entity.Event;
import com.sb.entity.User;
import com.sb.repository.UserRepository;
import com.sb.service.EventService;
import com.sb.service.TicketService;
import com.sb.util.LoggedInUserUtil;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final EventService eventService;
    private final UserRepository userRepository;
    private final TicketService ticketService;
    private final LoggedInUserUtil loggedInUserUtil;

    public TicketController(EventService eventService,
                            UserRepository userRepository,
                            TicketService ticketService,
                            LoggedInUserUtil loggedInUserUtil) {
        this.eventService = eventService;
        this.userRepository = userRepository;
        this.ticketService = ticketService;
        this.loggedInUserUtil=loggedInUserUtil;
    }

    // Register / payment page
    @GetMapping("/register/{eventId}")
    public String showRegisterPage(@PathVariable Long eventId,
                                   Model model,
                                   Authentication authentication) {

        Event event = eventService.getEventById(eventId);
        User user = loggedInUserUtil.getUser(authentication);

        model.addAttribute("event", event);
        model.addAttribute("user", user);

        return "ticket/register";
    }


    @PostMapping("/confirm")
    public String confirmTicket(@RequestParam Long eventId,
                                @RequestParam Double amount,
                                Authentication authentication) {

        User user = loggedInUserUtil.getUser(authentication);
        Event event = eventService.getEventById(eventId);

        ticketService.registerForEvent(event, user, amount);

        return "redirect:/dashboard";
    }


    // ONE SAFE METHOD FOR ALL LOGIN TYPES
    private User getLoggedInUser(Authentication authentication) {

        // OAuth2 login (Google)
        if (authentication.getPrincipal() instanceof OAuth2User oauthUser) {

            String email = oauthUser.getAttribute("email");
            String name  = oauthUser.getAttribute("name");

            return userRepository.findByEmail(email)
                    .orElseGet(() -> {
                        User user = new User();
                        user.setEmail(email);
                        user.setName(name);
                        user.setPassword("OAUTH2_USER"); // dummy
                        user.setPasswordChanged(true);
                        return userRepository.save(user);
                    });
        }

        // Normal form login
        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Authenticated user not found in DB"));
    }
}
