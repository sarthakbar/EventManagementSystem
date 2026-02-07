package com.sb.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sb.entity.Event;
import com.sb.entity.Feedback;
import com.sb.entity.User;
import com.sb.repository.FeedbackRepository;


@Service
public class FeedbackServiceImpl implements FeedbackService{

	  private final FeedbackRepository feedbackRepository;

	    public FeedbackServiceImpl(FeedbackRepository feedbackRepository) {
	        this.feedbackRepository = feedbackRepository;
	    }
	
	@Override
	public Feedback addFeedback(Event event, User loggedInUser, Integer rating, String comment) {
		
		feedbackRepository.findByEventAndUser(event, loggedInUser)
        .ifPresent(f -> {
            throw new RuntimeException("You have already submitted feedback for this event");
        });
		
		Feedback feedback=new Feedback();
		feedback.setEvent(event);
		feedback.setUser(loggedInUser);
		feedback.setRating(rating);
		feedback.setComment(comment);
		
		return feedbackRepository.save(feedback);
		
	}

	@Override
	public List<Feedback> getFeedbacksByEvent(Event event) {
		
		return feedbackRepository.findByEvent(event);
	}

}
