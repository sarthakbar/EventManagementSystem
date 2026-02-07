package com.sb.service;

import java.util.List;


import com.sb.entity.Event;
import com.sb.entity.Feedback;
import com.sb.entity.User;

public interface FeedbackService {

    // Add feedback (attendee)
    Feedback addFeedback(Event event, User loggedInUser, Integer rating, String comment);

    // Organizer: view all feedbacks for an event
    List<Feedback> getFeedbacksByEvent(Event event);
}
