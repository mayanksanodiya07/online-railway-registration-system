package com.booking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data 
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bookings")
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	private Long trainId;
	private Long authUserId;
	private int seatsBooked;
	private LocalDateTime bookingTime;
	
	@Column(nullable = false)
	private String status;
}
