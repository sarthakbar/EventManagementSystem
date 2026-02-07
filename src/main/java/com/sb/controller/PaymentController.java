package com.sb.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.Order;
import com.sb.entity.Event;
import com.sb.entity.User;
import com.sb.repository.UserRepository;
import com.sb.service.EventService;
import com.sb.service.RazorpayService;
import com.sb.service.TicketService;
import com.sb.util.LoggedInUserUtil;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final RazorpayService razorpayService;
    private final TicketService ticketService;
    private final EventService eventService;
    private final UserRepository userRepository;
    private final LoggedInUserUtil loggedInUserUtil;


    @Value("${razorpay.key.id}")
    private String razorpayKey;

    public PaymentController(RazorpayService razorpayService,
                             TicketService ticketService,
                             EventService eventService,
                             UserRepository userRepository,
                             LoggedInUserUtil loggedInUserUtil) {
        this.razorpayService = razorpayService;
        this.ticketService = ticketService;
        this.eventService = eventService;
        this.userRepository = userRepository;
        this.loggedInUserUtil = loggedInUserUtil;
    }

    
    @PostMapping("/create-order")
    public Map<String, Object> createOrder(@RequestParam Double amount) {

        Order order = razorpayService.createOrder(amount);

        Map<String, Object> response = new HashMap<>();
        response.put("orderId", order.get("id"));
        response.put("amount", order.get("amount"));
        response.put("currency", order.get("currency"));
        response.put("key", razorpayKey); // needed by frontend

        return response;
    }

    
    
    @PostMapping("/confirm")
    public String confirmPayment(@RequestParam Long eventId,
                                 @RequestParam String razorpayPaymentId,
                                 @RequestParam String razorpayOrderId,
                                 Authentication authentication) {

        User loggedInUser = loggedInUserUtil.getUser(authentication);

        Event event = eventService.getEventById(eventId);

        ticketService.registerPaidTicket(
                event,
                loggedInUser,
                razorpayPaymentId,
                razorpayOrderId
        );

        return "SUCCESS";
    }
    
}
