package com.sb.service;

import java.util.List;

import com.sb.entity.Event;
import com.sb.entity.Ticket;
import com.sb.entity.User;

public interface TicketService {

    // Register user for an event (after payment)
    Ticket registerForEvent(Event event, User loggedInUser, Double amount);

    // Attendee dashboard
    List<Ticket> getTicketsByUser(User user);

    // Organizer dashboard (tickets for one event)
    List<Ticket> getTicketsByEvent(Event event);
    
    void registerPaidTicket(Event event,
            User user,
            String razorpayPaymentId,
            String razorpayOrderId);
}
