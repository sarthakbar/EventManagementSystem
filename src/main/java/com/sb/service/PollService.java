package com.sb.service;

import java.util.List;

import com.sb.entity.Event;
import com.sb.entity.Poll;
import com.sb.entity.User;

public interface PollService {
    List<Poll> getPollsByEvent(Event event);
    void vote(Long optionId, User user);
    void createPoll(Long eventId,
            String question,
            List<String> options,
            User loggedInUser);

}
