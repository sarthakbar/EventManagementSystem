package com.sb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sb.entity.Event;
import com.sb.entity.Feedback;
import com.sb.entity.User;

public interface FeedbackRepository extends JpaRepository<Feedback, Long>{

	List<Feedback> findByEvent(Event event);
	
	Optional<Feedback> findByEventAndUser(Event event, User user);
}
