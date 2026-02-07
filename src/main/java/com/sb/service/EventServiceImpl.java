package com.sb.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sb.entity.Event;
import com.sb.entity.User;
import com.sb.repository.EventRepository;
import com.sb.repository.TicketRepository;


@Service
public class EventServiceImpl implements EventService{

	private EventRepository eventRepository;
	private TicketRepository ticketRepository;
	
	public EventServiceImpl(EventRepository eventRepository, TicketRepository ticketRepository) {
        this.eventRepository = eventRepository;
        this.ticketRepository=ticketRepository;
    }
	
	
	@Override
	public Event createEvent(Event event, User loggedInUser) {
		event.setCreatedBy(loggedInUser);
		return eventRepository.save(event);
	}

	@Override
	public Event updateEvent(Long eventId, Event updatedEvent, User loggedInUser) {
		
		Event existingEvent= getEventById(eventId);
		
		if(!existingEvent.getCreatedBy().getId().equals(loggedInUser.getId()))
		{
			throw new RuntimeException("You are not allowed to edit this event");
		}
		
		existingEvent.setTitle(updatedEvent.getTitle());
        existingEvent.setDescription(updatedEvent.getDescription());
        existingEvent.setEventDate(updatedEvent.getEventDate());
        existingEvent.setEventTime(updatedEvent.getEventTime());
        existingEvent.setLocation(updatedEvent.getLocation());
        existingEvent.setTicketPrice(updatedEvent.getTicketPrice());
        existingEvent.setPrivacy(updatedEvent.getPrivacy());

        return eventRepository.save(existingEvent);
	}

	@Override
	@Transactional
	public void deleteEvent(Long id, User user) {

	    Event event = eventRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Event not found"));

	    if (!event.getCreatedBy().getId().equals(user.getId())) {
	        throw new RuntimeException("Unauthorized");
	    }

	    ticketRepository.deleteByEvent(event); // ONLY tickets
	    eventRepository.delete(event);
	}


	@Override
	public List<Event> getEventsCreatedBy(User user) {
		
		return eventRepository.findByCreatedBy(user);
	}

	@Override
	public List<Event> getPublicEvents() {
		 
		return eventRepository.findByPrivacy("PUBLIC");
	}

	@Override
	public Event getEventById(Long eventId) {
		
		return eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));
	}

}
