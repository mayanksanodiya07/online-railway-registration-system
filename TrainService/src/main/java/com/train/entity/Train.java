package com.train.entity;
 
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Train {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Train name is required")
	private String trainName;

	@NotBlank(message = "Source is required")
	private String source;

	@NotBlank(message = "Destination is required")
	private String destination;

	@NotNull(message = "Departure time is required")
	private LocalDateTime departureTime;

	@NotNull(message = "Arrival time is required")
	private LocalDateTime arrivalTime;

	@NotNull(message = "Available seats are required")
	@Min(value = 0, message = "Available seats must be at least 0")
	private int availableSeats;

	@NotNull(message = "Booked seats are required")
	@Min(value = 0, message = "Booked seats must be at least 0")
	private int bookedSeats;
}