package com.sb.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.sb.entity.Comment;
import com.sb.entity.Event;
import com.sb.entity.User;
import com.sb.repository.UserRepository;
import com.sb.service.CommentService;
import com.sb.service.EventService;

@Controller
@RequestMapping("/events")
public class CommentController {

    private final CommentService commentService;
    private final EventService eventService;
    private final UserRepository userRepository;

    public CommentController(CommentService commentService,
                             EventService eventService,
                             UserRepository userRepository) {
        this.commentService = commentService;
        this.eventService = eventService;
        this.userRepository = userRepository;
    }

    // Show forum page for an event
    @GetMapping("/{eventId}/forum")
    public String showForum(@PathVariable Long eventId, Model model) {

        Event event = eventService.getEventById(eventId);
        List<Comment> comments = commentService.getCommentsByEvent(event);

        model.addAttribute("event", event);
        model.addAttribute("comments", comments);

        return "event/forum";
    }

    // Add comment
    @PostMapping("/{eventId}/forum")
    public String addComment(@PathVariable Long eventId,
                             @RequestParam String message,
                             Principal principal) {

        Event event = eventService.getEventById(eventId);
        User loggedInUser = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        commentService.addComment(event, loggedInUser, message);

        return "redirect:/events/" + eventId + "/forum";
    }
}
