package com.admin.model;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Train {
	
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
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTrainName() {
		return trainName;
	}
	public void setTrainName(String trainName) {
		this.trainName = trainName;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public LocalDateTime getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(LocalDateTime departureTime) {
		this.departureTime = departureTime;
	}
	public LocalDateTime getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(LocalDateTime arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	public int getAvailableSeats() {
		return availableSeats;
	}
	public void setAvailableSeats(int availableSeats) {
		this.availableSeats = availableSeats;
	}
	public int getBookedSeats() {
		return bookedSeats;
	}
	public void setBookedSeats(int bookedSeats) {
		this.bookedSeats = bookedSeats;
	}
	@Override
	public String toString() {
		return "Train [id=" + id + ", trainName=" + trainName + ", source=" + source + ", destination=" + destination
				+ ", departureTime=" + departureTime + ", arrivalTime=" + arrivalTime + ", availableSeats="
				+ availableSeats + ", bookedSeats=" + bookedSeats + "]";
	}
	 
}