package com.sb.controller;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.sb.entity.Event;
import com.sb.entity.User;
import com.sb.repository.UserRepository;
import com.sb.service.EventService;

@Controller
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;
    private final UserRepository userRepository;

    public EventController(EventService eventService,
                           UserRepository userRepository) {
        this.eventService = eventService;
        this.userRepository = userRepository;
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("event", new Event());
        return "event/create-event";
    }

    @PostMapping("/create")
    public String createEvent(@ModelAttribute Event event,
                              Authentication authentication) {

        User loggedInUser = getLoggedInUser(authentication);
        eventService.createEvent(event, loggedInUser);

        return "redirect:/dashboard";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Event event = eventService.getEventById(id);
        model.addAttribute("event", event);
        return "event/edit-event";
    }

    @PostMapping("/edit/{id}")
    public String updateEvent(@PathVariable Long id,
                              @ModelAttribute Event event,
                              Authentication authentication) {

        User loggedInUser = getLoggedInUser(authentication);
        eventService.updateEvent(id, event, loggedInUser);

        return "redirect:/dashboard";
    }

    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id,
                              Authentication authentication) {

        User loggedInUser = getLoggedInUser(authentication);
        eventService.deleteEvent(id, loggedInUser);

        return "redirect:/dashboard";
    }

    @GetMapping("/allEvents")
    public String showAllEvents(Model model) {
        model.addAttribute("events", eventService.getPublicEvents());
        return "event/list";
    }

    private User getLoggedInUser(Authentication authentication) {

        // OAuth2 Login
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

        // Normal Form Login
        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Authenticated user not found in DB"));
    }
}
