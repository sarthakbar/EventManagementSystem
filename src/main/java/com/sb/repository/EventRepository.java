package com.sb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sb.entity.Event;
import com.sb.entity.User;

public interface EventRepository extends JpaRepository<Event, Long>{

	
	//organiser dashboard
	List<Event> findByCreatedBy(User user);
	
	List<Event> findByPrivacy(String privacy);
	
}
