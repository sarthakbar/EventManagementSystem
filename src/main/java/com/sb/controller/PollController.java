package com.sb.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.sb.entity.Event;
import com.sb.entity.Poll;
import com.sb.entity.User;
import com.sb.repository.UserRepository;
import com.sb.service.EventService;
import com.sb.service.PollService;

@Controller
@RequestMapping("/polls")
public class PollController {

    private final PollService pollService;
    private final EventService eventService;
    private final UserRepository userRepository;

    public PollController(PollService pollService,
                          EventService eventService,
                          UserRepository userRepository) {
        this.pollService = pollService;
        this.eventService = eventService;
        this.userRepository = userRepository;
    }

    // SHOW ALL POLLS OF AN EVENT
    @GetMapping("/event/{eventId}")
    public String showPolls(@PathVariable Long eventId, Model model) {

        Event event = eventService.getEventById(eventId);
        List<Poll> polls = pollService.getPollsByEvent(event);

        model.addAttribute("event", event);
        model.addAttribute("polls", polls);

        return "poll/polls";
    }

    // VOTE
    @PostMapping("/vote")
    public String vote(@RequestParam Long optionId,
                       @RequestParam Long eventId,
                       Principal principal) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        pollService.vote(optionId, user);

        return "redirect:/polls/event/" + eventId;
    }

    //SHOW CREATE POLL PAGE
    @GetMapping("/create/{eventId}")
    public String showCreatePoll(@PathVariable Long eventId, Model model) {

        Event event = eventService.getEventById(eventId);

        model.addAttribute("event", event);

        return "poll/create-poll";
    }

    //CREATE POLL
    @PostMapping("/create/{eventId}")
    public String createPoll(@PathVariable Long eventId,
                             @RequestParam String question,
                             @RequestParam List<String> options,
                             Principal principal) {

        if (options == null || options.isEmpty()) {
            throw new RuntimeException("Poll must have at least one option");
        }

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        pollService.createPoll(eventId, question, options, user);

        return "redirect:/polls/event/" + eventId;
    }
}
