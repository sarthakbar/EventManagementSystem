package com.sb.service;

import java.util.List;

import com.sb.entity.Event;
import com.sb.entity.User;

public interface EventService {

	// Create event (organizer)
	Event createEvent(Event event, User loggedInUser);

	// Update event (only creator)
	Event updateEvent(Long eventId, Event updatedEvent, User loggedInUser);

	// Delete event (only creator)
	void deleteEvent(Long eventId, User loggedInUser);

	// Organizer dashboard
	List<Event> getEventsCreatedBy(User user);

	// Event discovery
	List<Event> getPublicEvents();

	// Get single event
	Event getEventById(Long eventId);
}
