package com.sb.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.sb.entity.Event;
import com.sb.entity.Feedback;
import com.sb.entity.User;
import com.sb.repository.UserRepository;
import com.sb.service.EventService;
import com.sb.service.FeedbackService;

@Controller
@RequestMapping("/events")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final EventService eventService;
    private final UserRepository userRepository;

    public FeedbackController(FeedbackService feedbackService,
                              EventService eventService,
                              UserRepository userRepository) {
        this.feedbackService = feedbackService;
        this.eventService = eventService;
        this.userRepository = userRepository;
    }

    // 🔹 Show feedback page
    @GetMapping("/{eventId}/feedback")
    public String showFeedbackPage(@PathVariable Long eventId, Model model) {

        Event event = eventService.getEventById(eventId);
        List<Feedback> feedbacks = feedbackService.getFeedbacksByEvent(event);

        model.addAttribute("event", event);
        model.addAttribute("feedbacks", feedbacks);

        return "event/feedback";
    }

    // 🔹 Submit feedback
    @PostMapping("/{eventId}/feedback")
    public String submitFeedback(@PathVariable Long eventId,
                                 @RequestParam Integer rating,
                                 @RequestParam String comment,
                                 Principal principal) {

        Event event = eventService.getEventById(eventId);
        User loggedInUser = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        feedbackService.addFeedback(event, loggedInUser, rating, comment);

        return "redirect:/events/" + eventId + "/feedback";
    }
}
