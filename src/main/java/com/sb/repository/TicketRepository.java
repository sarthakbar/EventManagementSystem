package com.sb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sb.entity.Event;
import com.sb.entity.Ticket;
import com.sb.entity.User;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

	//attendee dashboard
	List<Ticket> findByUser(User user);
	
	List<Ticket> findByEvent(Event event);
	
	Optional<Ticket> findByUserAndEvent(User user, Event event);
	
	boolean existsByEventAndUser(Event event, User user);

	void deleteByEvent(Event event);

	
}
