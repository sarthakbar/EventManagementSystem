package com.sb.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sb.entity.Event;
import com.sb.entity.Ticket;
import com.sb.entity.User;
import com.sb.repository.TicketRepository;

@Service
public class TicketServiceImpl implements TicketService {

	private TicketRepository ticketRepository;

	public TicketServiceImpl(TicketRepository ticketRepository) {
		this.ticketRepository = ticketRepository;
	}

	@Override
	public Ticket registerForEvent(Event event, User loggedInUser, Double amount) {
		ticketRepository.findByUserAndEvent(loggedInUser, event).ifPresent(t -> {
			throw new RuntimeException("You already registered for this event");
		});

		Ticket ticket = new Ticket();
		ticket.setUser(loggedInUser);
		ticket.setEvent(event);
		ticket.setAmount(amount);
		ticket.setPaymentStatus(amount > 0 ? "PAID" : "FREE");
		ticket.setPaymentTime(LocalDateTime.now());

		return ticketRepository.save(ticket);
	}

	@Override
	public List<Ticket> getTicketsByUser(User user) {

		return ticketRepository.findByUser(user);
	}

	@Override
	public List<Ticket> getTicketsByEvent(Event event) {
		return ticketRepository.findByEvent(event);
	}

	@Override
	public void registerPaidTicket(Event event, User user, String razorpayPaymentId, String razorpayOrderId) {

// prevent duplicate registration
		boolean alreadyRegistered = ticketRepository.existsByEventAndUser(event, user);

		if (alreadyRegistered) {
			throw new RuntimeException("You already registered for this event");
		}

		Ticket ticket = new Ticket();
		ticket.setEvent(event);
		ticket.setUser(user);
		ticket.setAmount(event.getTicketPrice());
		ticket.setPaymentStatus("PAID");
		ticket.setPaymentTime(LocalDateTime.now());
		ticket.setRazorPayPaymentId(razorpayPaymentId);
		ticket.setRazorPayOrderId(razorpayOrderId);

		ticketRepository.save(ticket);
	}

}
