package com.sb.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;


@Entity
@Data
public class Ticket {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private User user;   //attendee
	
	@ManyToOne
	private Event event;
	
	private Double amount;
	
	private String paymentStatus;
	
	private String razorPayOrderId;
	private String razorPayPaymentId;
	
	private LocalDateTime paymentTime;
}
