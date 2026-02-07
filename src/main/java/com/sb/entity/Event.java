package com.sb.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;


@Entity
@Data
public class Event {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	private String title;
	private String description;
	private LocalDate eventDate;
	private LocalTime eventTime;
	private String location;
	private Double ticketPrice;
	private String privacy;  // public / private
	
	@ManyToOne
	public User createdBy;
}
